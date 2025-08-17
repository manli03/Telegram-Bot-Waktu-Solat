package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Represents a Telegram Bot that extends TelegramLongPollingBot.
 * This bot handles text messages and callback queries from users.
 *
 * @author Aiman Norazli
 */
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBot bot; // Store the instance of TelegramBot
    private final KeyboardButton kb; // KeyboardButton instance
    private final String normal = "STATE_NORMAL";
    private final String busy = "STATE_EXPECTING_CALLBACK";
    private final String botToken = System.getenv("BOT_TOKEN");
    private final String botUsername = System.getenv("BOT_USERNAME");



    /**
     * Constructs a TelegramBot instance.
     * Initializes the TelegramBot instance and passes it to KeyboardButton.
     */
    public TelegramBot() {
        // Initialize the TelegramBot instance and pass it to KeyboardButton
        this.bot = this;
        this.kb = new KeyboardButton(bot, botUsername);
    }

    /**
     * Invoked when an update is received.
     * Handles text messages and callback queries.
     * @param update The received update
     */
    @Override
    public void onUpdateReceived(Update update) {

        // Determine if the update is from a text message
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        }

        // Determine if the update is from a callback query
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    /**
     * Handles text messages received from users.
     * @param update The received update
     */
    private void handleTextMessage(Update update) {
        // Extract the message text, chat ID, and firstName and username
        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        String firstName = update.getMessage().getFrom().getFirstName();
        String username = update.getMessage().getFrom().getUserName();
        Integer buttonMessageId;
        String currentState = UserInformation.getUserCurrentState(chatId);
        String lastSentMessage = UserInformation.getLastSendMessage(chatId);
        Integer lastButtonMessageId = UserInformation.getLastButtonMessageId(chatId);
        ResendKeyboardButton rkb = new ResendKeyboardButton(bot, botUsername);

        // Update current username
        UserInformation.setUserUsername(chatId, username);

        switch (currentState) {

            case busy:
                // The user was expected to interact with a callback query but sent a text message instead
                kb.deleteMessage(chatId, lastButtonMessageId); // Reset last button
                kb.sendMessage(chatId, Language.errorMessage(chatId));

                if (lastSentMessage != null) {
                    switch (lastSentMessage) {
                        case "nextButton":
                            buttonMessageId = rkb.resendNextButton(chatId);
                            UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "stateMenu":
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.state(chatId));
                            UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "zoneMenu":
                            buttonMessageId = rkb.resendZoneMenu(chatId, UserInformation.getUserState(chatId));
                            UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "changeLanguage":
                            buttonMessageId = kb.changeLanguage(chatId);
                            UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + lastSentMessage);
                    }
                }
                break; // Stop the switch

            case "start":
                // If user keep spamming /start command
                kb.deleteMessage(chatId, lastButtonMessageId);
                kb.sendMessage(chatId, Language.errorMessage(chatId));
                buttonMessageId = kb.sendStartupMessage(chatId, firstName);
                UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                break;

            case normal:
                // Handle recognized commands
                switch (messageText) {
                    case "/start":
                        buttonMessageId = kb.sendStartupMessage(chatId, firstName);
                        UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                        UserInformation.setUserCurrentState(chatId, "start");
                        break;
                    case "/prayer_time":
                    case "/waktu_solat":
                        String prayerTimes = WaktuSolatAPI.getPrayerTimes(chatId);
                        if (prayerTimes != null) {
                            rkb.reSendwaktuSolatToTelegram(prayerTimes, chatId);
                            UserInformation.setUserCurrentState(chatId, normal);
                            kb.sendMessage(chatId, Language.showMenuMessage(chatId));
                        }
                        else {
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.chooseStateFirst(chatId));
                            UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                            UserInformation.setLastSendMessage(chatId, "stateMenu");
                            UserInformation.setUserCurrentState(chatId, busy);
                        }
                        break;
                    case "/switch_state":
                    case "/tukar_negeri":
                        buttonMessageId = rkb.resendStateMenu(chatId, Language.state(chatId));
                        UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                        UserInformation.setLastSendMessage(chatId, "stateMenu");
                        UserInformation.setUserCurrentState(chatId, busy);
                        break;
                    case "/switch_area":
                    case "/tukar_kawasan":
                        if(UserInformation.getUserState(chatId) != null)
                            buttonMessageId = rkb.resendZoneMenu(chatId, UserInformation.getUserState(chatId));
                        else
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.chooseStateFirst(chatId));
                        UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                        UserInformation.setLastSendMessage(chatId, "zoneMenu");
                        UserInformation.setUserCurrentState(chatId, busy);
                        break;
                    case "/change_language":
                    case "/tukar_bahasa":
                        buttonMessageId = kb.changeLanguage(chatId);
                        UserInformation.setLastButtonMessageId(chatId, buttonMessageId);
                        UserInformation.setLastSendMessage(chatId, "changeLanguage");
                        UserInformation.setUserCurrentState(chatId, busy);
                        break;
                    case "/source":
                    case "/sumber":
                        kb.sendSource(chatId);
                        kb.sendMessage(chatId, Language.showMenuMessage(chatId));
                        break;
                    case "/show_menu":
                    case "/papar_menu":
                        kb.sendListMenu(chatId);
                        break;
                    default:
                        // Unrecognized command: send error message
                        kb.sendMessage(chatId, Language.errorMessage(chatId));
                        break;
                }
        }
    }

    /**
     * Handles callback queries received from users.
     * @param update The received update
     */
    private void handleCallbackQuery(Update update) {
        // Extract callback data, chat ID, and message ID
        String data = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        // Handle callback queries based on the data
        if (data.equals("Bahasa Melayu") || data.equals("English")) {
            UserInformation.setUserLanguagePreference(chatId, data);
            kb.nextButton(chatId, messageId);
            UserInformation.setLastButtonMessageId(chatId, messageId);
            UserInformation.setUserCurrentState(chatId, busy);
            UserInformation.setLastSendMessage(chatId, "nextButton");
        } else if (data.equals("next")) {
            kb.sendStateMenu(chatId, messageId);
            UserInformation.setLastButtonMessageId(chatId, messageId);
            UserInformation.setLastSendMessage(chatId, "stateMenu");
        } else if (data.equals("malay") || data.equals("inggeris")) {
            UserInformation.setUserLanguagePreference(chatId, data);
            kb.informLanguage(chatId, messageId);
            kb.sendMessage(chatId, Language.showMenuMessage(chatId));
            UserInformation.setUserCurrentState(chatId, normal);
        } else if (Location.checkState(data)) {
            UserInformation.setUserState(chatId, data);
            kb.sendZoneMenu(chatId, messageId, data);
            UserInformation.setLastButtonMessageId(chatId, messageId);
            UserInformation.setLastSendMessage(chatId, "zoneMenu");
        } else if (Location.checkLocation(data)) {
            // Handle location data and send prayer times
            UserInformation.setUserZone(chatId, data);
            String prayerTimes = WaktuSolatAPI.getPrayerTimes(chatId);
            kb.sendwaktuSolatToTelegram(prayerTimes, chatId, messageId);
            UserInformation.setUserCurrentState(chatId, normal);
            kb.sendMessage(chatId, Language.showMenuMessage(chatId));
        } else {
            // Handle unrecognized data in the callback query
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(Language.failedZoneArea(chatId));
            UserInformation.setUserCurrentState(chatId, normal);
            System.out.println(data);

            try {
                execute(editMessageText);
            } catch (Exception e) {
                System.err.println("Failed to send error message: " + e.getMessage());
            }
        }
    }

    /**
     * Gets the username of the bot.
     * @return The bot's username
     */
    @Override
    public String getBotUsername() {
        if (botUsername == null || botUsername.isEmpty()) {
            throw new IllegalStateException("Bot username is not set. Please provide the BOT_USERNAME environment variable.");
        }
        return botUsername;
    }

    /**
     * Gets the token of the bot.
     * @return The bot's token
     */
    @Override
    public String getBotToken() {
        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalStateException("Bot token is not set. Please provide the BOT_TOKEN environment variable.");
        }
        return botToken;
    }
}
