package my.uum;

import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * The Main class is the entry point for the application.
 * @author Aiman Norazli
 */
public class Main {

    /**
     * The main method starts the webhook server and registers the webhook with Telegram.
     * Reads WEBHOOK_URL (public https base URL) and optionally WEBHOOK_PORT (default 8080).
     * The webhook secret token is derived from BOT_TOKEN, so no separate secret needs configuring.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            String webhookUrl = requireEnv("WEBHOOK_URL").replaceAll("/+$", "");
            String portEnv = System.getenv("WEBHOOK_PORT");
            int port = portEnv == null || portEnv.isBlank() ? 8080 : Integer.parseInt(portEnv.trim());

            TelegramBot bot = new TelegramBot();
            String secret = deriveWebhookSecret(bot.getBotToken());
            String path = "/callback/" + TelegramBot.BOT_PATH;
            new WebhookServer(bot, secret).start(port, path);
            System.out.println("Webhook server listening on port " + port + " at " + path);

            // Library appends /callback/{botPath} to the URL given here
            bot.setWebhook(SetWebhook.builder().url(webhookUrl).secretToken(secret).build());
            System.out.println("Webhook registered: " + webhookUrl + path);
            System.out.println("Bot is running...");
        } catch (Exception e) {
            System.err.println("Failed to start bot:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Derives the webhook secret token from the bot token: hex(sha256("secret:" + botToken)).
     * The result is 64 characters of [0-9a-f], which satisfies Telegram's secret_token rules.
     */
    static String deriveWebhookSecret(String botToken) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(("secret:" + botToken).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e); // guaranteed by the JDK spec
        }
    }

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " environment variable is required");
        }
        return value.trim();
    }
}
