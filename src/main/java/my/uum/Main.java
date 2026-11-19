package my.uum;

import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

/**
 * The Main class is the entry point for the application.
 * @author Aiman Norazli
 */
public class Main {

    /**
     * The main method starts the webhook server and registers the webhook with Telegram.
     * Reads WEBHOOK_URL (public https base URL), WEBHOOK_SECRET and optionally WEBHOOK_PORT (default 8080).
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            String webhookUrl = requireEnv("WEBHOOK_URL").replaceAll("/+$", "");
            String secret = requireEnv("WEBHOOK_SECRET");
            if (!secret.matches("[A-Za-z0-9_-]{1,256}")) {
                throw new IllegalStateException("WEBHOOK_SECRET must be 1-256 characters of A-Z, a-z, 0-9, _ or -");
            }
            String portEnv = System.getenv("WEBHOOK_PORT");
            int port = portEnv == null || portEnv.isBlank() ? 8080 : Integer.parseInt(portEnv.trim());

            TelegramBot bot = new TelegramBot();
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

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " environment variable is required");
        }
        return value.trim();
    }
}
