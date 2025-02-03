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
    private final UserInformation user = new UserInformation();
    private final String botToken = System.getenv("BOT_TOKEN");



    /**
     * Constructs a TelegramBot instance.
     * Initializes the TelegramBot instance and passes it to KeyboardButton.
     */
    public TelegramBot() {
        // Initialize the TelegramBot instance and pass it to KeyboardButton
        this.bot = this;
        this.kb = new KeyboardButton(bot);
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
        // Extract the message text, chat ID, and username
        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        String username = update.getMessage().getFrom().getFirstName();
        Integer buttonMessageId;
        String currentState = user.getUserCurrentState(chatId);
        String lastSentMessage = user.getLastSendMessage(chatId);
        Integer lastButtonMessageId = user.getLastButtonMessageId(chatId);
        ResendKeyboardButton rkb = new ResendKeyboardButton(bot);
        WaktuSolatAPI api = new WaktuSolatAPI();


        switch (currentState) {

            case busy:
                // The user was expected to interact with a callback query but sent a text message instead
                kb.deleteMessage(chatId, lastButtonMessageId); // Reset last button
                kb.sendMessage(chatId, Language.errorMessage(chatId));

                if (lastSentMessage != null) {
                    switch (lastSentMessage) {
                        case "nextButton":
                            buttonMessageId = rkb.resendNextButton(chatId);
                            user.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "stateMenu":
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.state(chatId));
                            user.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "zoneMenu":
                            buttonMessageId = rkb.resendZoneMenu(chatId, user.getUserState(chatId));
                            user.setLastButtonMessageId(chatId, buttonMessageId);
                            break;
                        case "changeLanguage":
                            buttonMessageId = kb.changeLanguage(chatId);
                            user.setLastButtonMessageId(chatId, buttonMessageId);
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
                buttonMessageId = kb.sendStartupMessage(chatId, username);
                user.setLastButtonMessageId(chatId, buttonMessageId);
                break;

            case normal:
                // Handle recognized commands
                switch (messageText) {
                    case "/start":
                        buttonMessageId = kb.sendStartupMessage(chatId, username);
                        user.setLastButtonMessageId(chatId, buttonMessageId);
                        user.setUserCurrentState(chatId, "start");
                        break;
                    case "/prayer_time":
                    case "/waktu_solat":
                        String prayerTimes = api.getPrayerTimes(chatId);
                        if (prayerTimes != null) {
                            rkb.reSendwaktuSolatToTelegram(prayerTimes, chatId);
                            user.setUserCurrentState(chatId, normal);
                            kb.sendMessage(chatId, Language.showMenuMessage(chatId));
                        }
                        else {
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.chooseStateFirst(chatId));
                            user.setLastButtonMessageId(chatId, buttonMessageId);
                            user.setLastSendMessage(chatId, "stateMenu");
                            user.setUserCurrentState(chatId, busy);
                        }
                        break;
                    case "/switch_state":
                    case "/tukar_negeri":
                        buttonMessageId = rkb.resendStateMenu(chatId, Language.state(chatId));
                        user.setLastButtonMessageId(chatId, buttonMessageId);
                        user.setLastSendMessage(chatId, "stateMenu");
                        user.setUserCurrentState(chatId, busy);
                        break;
                    case "/switch_area":
                    case "/tukar_kawasan":
                        if(user.getUserState(chatId) != null)
                            buttonMessageId = rkb.resendZoneMenu(chatId, user.getUserState(chatId));
                        else
                            buttonMessageId = rkb.resendStateMenu(chatId, Language.chooseStateFirst(chatId));
                        user.setLastButtonMessageId(chatId, buttonMessageId);
                        user.setLastSendMessage(chatId, "zoneMenu");
                        user.setUserCurrentState(chatId, busy);
                        break;
                    case "/change_language":
                    case "/tukar_bahasa":
                        buttonMessageId = kb.changeLanguage(chatId);
                        user.setLastButtonMessageId(chatId, buttonMessageId);
                        user.setLastSendMessage(chatId, "changeLanguage");
                        user.setUserCurrentState(chatId, busy);
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
        WaktuSolatAPI api = new WaktuSolatAPI();
        Location location = new Location();

        // Handle callback queries based on the data
        if (data.equals("Bahasa Melayu") || data.equals("English")) {
            user.setUserLanguagePreference(chatId, data);
            kb.nextButton(chatId, messageId);
            user.setLastButtonMessageId(chatId, messageId);
            user.setUserCurrentState(chatId, busy);
            user.setLastSendMessage(chatId, "nextButton");
        } else if (data.equals("next")) {
            kb.sendStateMenu(chatId, messageId);
            user.setLastButtonMessageId(chatId, messageId);
            user.setLastSendMessage(chatId, "stateMenu");
        } else if (data.equals("malay") || data.equals("inggeris")) {
            user.setUserLanguagePreference(chatId, data);
            kb.informLanguage(chatId, messageId);
            kb.sendMessage(chatId, Language.showMenuMessage(chatId));
            user.setUserCurrentState(chatId, normal);
        } else if (location.checkState(data)) {
            user.setUserState(chatId, data);
            kb.sendZoneMenu(chatId, messageId, data);
            user.setLastButtonMessageId(chatId, messageId);
            user.setLastSendMessage(chatId, "zoneMenu");
        } else if (location.checkLocation(data)) {
            // Handle location data and send prayer times
            user.setUserZone(chatId, data);
            String prayerTimes = api.getPrayerTimes(chatId);
            kb.sendwaktuSolatToTelegram(prayerTimes, chatId, messageId);
            user.setUserCurrentState(chatId, normal);
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
        return "s294214_bot";
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
