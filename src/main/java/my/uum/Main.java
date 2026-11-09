package my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * The Main class is the entry point for the application.
 * @author Aiman Norazli
 */
public class Main {

    /**
     * The main method initializes and starts the Telegram bot.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Initialize the Telegram bot using TelegramBotsApi
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());
            System.out.println("Bot is running...");
        } catch (TelegramApiException e) {
            System.err.println("Telegram API Exception:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Exception:");
            e.printStackTrace();
        }
    }
}
