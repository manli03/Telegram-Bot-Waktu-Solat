package my.uum;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Minimal HTTP server that receives Telegram webhook updates.
 * Requests must carry the X-Telegram-Bot-Api-Secret-Token header registered with setWebhook.
 * Updates are acknowledged immediately and processed one at a time on a worker thread,
 * so slow handlers never make Telegram time out and retry.
 *
 * @author Aiman Norazli
 */
public class WebhookServer {

    private static final String SECRET_HEADER = "X-Telegram-Bot-Api-Secret-Token";

    private final TelegramBot bot;
    private final byte[] secret;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // Single thread keeps updates in order, the same as long polling did
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private HttpServer server;

    /**
     * @param bot    The bot that handles updates
     * @param secret The secret token Telegram sends with every request
     */
    public WebhookServer(TelegramBot bot, String secret) {
        this.bot = bot;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Starts listening on all interfaces.
     * @param port Port to listen on
     * @param path URL path Telegram posts updates to
     * @throws IOException If the port cannot be bound
     */
    public void start(int port, String path) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(path, this::handleUpdate);
        server.createContext("/", exchange -> respond(exchange, 200, "ok")); // health check
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            respond(exchange, 405, "");
            return;
        }
        String header = exchange.getRequestHeaders().getFirst(SECRET_HEADER);
        if (header == null || !MessageDigest.isEqual(secret, header.getBytes(StandardCharsets.UTF_8))) {
            respond(exchange, 401, "");
            return;
        }

        Update update;
        try (InputStream body = exchange.getRequestBody()) {
            update = mapper.readValue(body, Update.class);
        } catch (IOException e) {
            System.err.println("Invalid update payload: " + e.getMessage());
            respond(exchange, 400, "");
            return;
        }

        respond(exchange, 200, "");
        worker.submit(() -> {
            try {
                bot.onWebhookUpdateReceived(update);
            } catch (Exception e) {
                System.err.println("Error handling update " + update.getUpdateId() + ":");
                e.printStackTrace();
            }
        });
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        if (bytes.length > 0) {
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }

    private void stop() {
        server.stop(1);
        worker.shutdown();
        try {
            worker.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
