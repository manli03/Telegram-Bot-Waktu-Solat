package my.uum;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class for handling keyboard buttons in the Telegram bot.
 * Provides methods to send various types of messages with inline keyboard buttons.
 *
 * @author Aiman Norazli
 */
public class KeyboardButton {

    private final TelegramBot bot;
    private final String botUsername;

    /**
     * Constructs a KeyboardButton instance with a reference to the TelegramBot instance.
     * @param bot The TelegramBot instance
     */
    public KeyboardButton(TelegramBot bot, String botUsername) {
        this.bot = bot;
        this.botUsername = botUsername;
    }

    /**
     * Sends a startup message with language selection buttons.
     * @param chatId The chat ID to send the message to
     * @param firstName The firstName of the user
     * @return The message ID of the sent message
     */
    public Integer sendStartupMessage(String chatId, String firstName) {
        //Create welcome message
        String welcomeMessage =
                ("English:\nAssalamualaikum " + firstName + ", I am the *Prayer Times Bot \\(MY\\)*\\.\n\n" +
                        "To begin, please choose your preferred language\\.\n\n\n" +
                        "Bahasa Melayu:\nAssalamualaikum " + firstName + ", saya adalah *Bot Waktu Solat \\(MY\\)*\\.\n\n" +
                        "Untuk bermula, sila pilih bahasa yang anda mahu gunakan\\.");

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Bahasa Melayu");
        button1.setCallbackData("Bahasa Melayu");
        row.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("English");
        button2.setCallbackData("English");
        row.add(button2);

        keyboard.add(row);

        // Create the keyboard markup
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(welcomeMessage);
        message.setReplyMarkup(markup);
        message.setParseMode("MarkdownV2");

        // Send the message with buttons
        try {
            // Send the message and get the response
            Message sentMessage = bot.execute(message);

            // Return the message ID
            return sentMessage.getMessageId();
        } catch (Exception e) {
            System.err.println("Failed to send welcome message: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }

    /**
     * Sends a message with a list menu.
     * @param chatId The chat ID to send the message to
     */
    public void sendListMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setParseMode("MarkdownV2");
        message.setText(Language.menuMessage(chatId));

        try {
            bot.execute(message);
        } catch (TelegramApiRequestException e) {
            System.err.println("Failed to send message: " + e.getApiResponse());
        } catch (TelegramApiException e) {
            System.err.println("Failed to send menu message: " + e.getMessage());
        }
    }

    /**
     * Sends a "Next" button to the user.
     * @param chatId The chat ID to send the button to
     * @param messageId The ID of the message to update
     */
    public void nextButton(String chatId, Integer messageId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(Language.nextButton(chatId));
        button.setCallbackData("next");
        row1.add(button);

        keyboard.add(row1);

        // Create the keyboard markup
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(Language.informLanguage(chatId, botUsername));
        editMessageText.setReplyMarkup(markup);
        editMessageText.setParseMode("MarkdownV2");

        try {
            bot.execute(editMessageText);
        } catch (Exception e) {
            System.err.println("Failed to show next: " + e.getMessage());
        }
    }

    /**
     * Sends a state menu with selectable state buttons.
     * @param chatId The chat ID to send the menu to
     * @param messageId The ID of the message to update
     */
    public void sendStateMenu(String chatId, Integer messageId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        String[][] buttons = {
                {"Johor", "Kedah"},
                {"Kelantan", "Melaka"},
                {"Negeri Sembilan", "Pahang"},
                {"Perlis", "Pulau Pinang"},
                {"Perak", "Sabah"},
                {"Selangor", "Sarawak"},
                {"Terengganu", "Wilayah Persekutuan"}
        };

        for (String[] buttonRow : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String buttonText : buttonRow) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonText);
                button.setCallbackData(buttonText);
                row.add(button);
            }
            keyboard.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the edit message
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId); // Update existing message
        editMessageText.setText(Language.state(chatId));
        editMessageText.setReplyMarkup(markup);

        try {
            bot.execute(editMessageText);
        } catch (Exception e) {
            System.err.println("Failed to show state: " + e.getMessage());
        }
    }

    /**
     * Sends a zone menu with selectable zone buttons based on the selected state.
     * @param chatId The chat ID to send the menu to
     * @param messageId The ID of the message to update
     * @param stateMessage The selected state message
     */
    public void sendZoneMenu(String chatId, Integer messageId, String stateMessage) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (!(stateMessage.equals("Pulau Pinang") || stateMessage.equals("Melaka"))) {
            String[][] buttons;

            switch (stateMessage) {
                case "Johor":
                    buttons = State.Johor();
                    break;
                case "Kedah":
                    buttons = State.Kedah();
                    break;
                case "Kelantan":
                    buttons = State.Kelantan();
                    break;
                case "Negeri Sembilan":
                    buttons = State.NegeriSembilan();
                    break;
                case "Pahang":
                    buttons = State.Pahang();
                    break;
                case "Perlis":
                    buttons = State.Perlis();
                    break;
                case "Perak":
                    buttons = State.Perak();
                    break;
                case "Sabah":
                    buttons = State.Sabah();
                    break;
                case "Selangor":
                    buttons = State.Selangor();
                    break;
                case "Sarawak":
                    buttons = State.Sarawak();
                    break;
                case "Terengganu":
                    buttons = State.Terengganu();
                    break;
                default:
                    buttons = State.WilayahPersekutuan();
                    break;
            }

            for (String[] buttonRow : buttons) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                for (String buttonText : buttonRow) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(buttonText);
                    button.setCallbackData(buttonText);
                    row.add(button);
                }
                keyboard.add(row);
            }
        }
        else {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            if(stateMessage.equals("Melaka")) {
                button.setText(Language.zonMelaka(chatId));
                button.setCallbackData("melak");
            }
            else {
                button.setText(Language.zonPenang(chatId));
                button.setCallbackData("penang");
            }
            row.add(button);
            keyboard.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the edit message
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId); // Update existing message
        editMessageText.setText(Language.zone(chatId));
        editMessageText.setReplyMarkup(markup);

        try {
            bot.execute(editMessageText);
        } catch (Exception e) {
            System.err.println("Failed to show zone: " + e.getMessage());
        }
    }

    /**
     * Sends prayer times information to the user.
     * @param waktuSolat The prayer times information
     * @param chatId The chat ID to send the message to
     * @param messageId The ID of the message to update
     */
    public void sendwaktuSolatToTelegram(String waktuSolat, String chatId, Integer messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setParseMode("MarkdownV2");
        if (waktuSolat != null) {
            editMessageText.setText(waktuSolat);
        } else {
            editMessageText.setText(Language.failedPrayerMessage(chatId));
        }

        try {
            bot.execute(editMessageText);
        } catch (TelegramApiRequestException e) {
            System.err.println("Failed to send message: " + e.getApiResponse());
        } catch (TelegramApiException e) {
            System.err.println("Failed to send prayer times message: " + e.getMessage());
        }
    }

    /**
     * Sends a language selection menu to the user.
     * @param chatId The chat ID to send the menu to
     * @return The message ID of the sent message
     */
    public Integer changeLanguage(String chatId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Bahasa Melayu");
        button1.setCallbackData("malay");
        row.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("English");
        button2.setCallbackData("inggeris");
        row.add(button2);

        keyboard.add(row);

        // Create the keyboard markup
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(Language.chooseLanguage(chatId));
        message.setReplyMarkup(markup);

        try {
            // Send the message and get the response
            Message sentMessage = bot.execute(message);

            // Return the message ID
            return sentMessage.getMessageId();
        } catch (Exception e) {
            System.err.println("Failed to show language option: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }

    /**
     * Informs the user about the selected language.
     * @param chatId The chat ID to send the message to
     * @param messageId The ID of the message to update
     */
    public void informLanguage(String chatId, Integer messageId) {// Create the edit message
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(Language.informLanguage(chatId, botUsername));
        editMessageText.setParseMode("MarkdownV2");

        try {
            bot.execute(editMessageText);
        } catch (Exception e) {
            System.err.println("Failed to send informLanguage: " + e.getMessage());
        }
    }

    /**
     * Sends the source information to the user.
     * @param chatId The chat ID to send the message to
     */
    public void sendSource(String chatId) {// Create the message
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(Language.source(chatId, botUsername));
        message.setParseMode("MarkdownV2");

        try {
            bot.execute(message);
        } catch (Exception e) {
            System.err.println("Failed to send informLanguage: " + e.getMessage());
        }
    }

    /**
     * Sends a text message to the user.
     * @param chatId The chat ID to send the message to
     * @param textMessage The text message to send
     */
    public void sendMessage(String chatId, String textMessage) {
        // Create the message
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textMessage);

        try {
            bot.execute(message);
        } catch (Exception e) {
            System.err.println("Failed to send errorMessage: " + textMessage + "\n" + e.getMessage());
        }
    }

    /**
     * Deletes a message sent by the bot.
     * @param chatId The chat ID of the message
     * @param messageId The ID of the message to delete
     */
    public void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            bot.execute(deleteMessage);
        } catch (Exception e) {
            System.out.println("Failed to delete message (Normal error ignore): " + e.getMessage());
        }
    }
}



/**
 * Represents a class for resending keyboard buttons in the Telegram bot.
 * Provides methods to resend specific types of messages with inline keyboard buttons.
 */
class ResendKeyboardButton {
    private final TelegramBot bot;
    private final String botUsername;

    /**
     * Constructs a ResendKeyboardButton instance with a reference to the TelegramBot instance.
     * @param bot The TelegramBot instance
     */
    public ResendKeyboardButton(TelegramBot bot, String botUsername) {
        this.bot = bot;
        this.botUsername = botUsername;
    }

    /**
     * Resends the "Next" button message to the user.
     * @param chatId The chat ID to send the message to
     * @return The message ID of the sent message
     */
    public Integer resendNextButton(String chatId) {
        SendMessage message = new SendMessage();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(Language.nextButton(chatId));
        button.setCallbackData("next");
        row1.add(button);

        keyboard.add(row1);

        // Create the keyboard markup
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        message.setChatId(chatId);
        message.setText(Language.informLanguage(chatId, botUsername));
        message.setReplyMarkup(markup);
        message.setParseMode("MarkdownV2");

        try {
            // Send the message and get the response
            Message sentMessage = bot.execute(message);

            // Return the message ID
            return sentMessage.getMessageId();
        } catch (Exception e) {
            System.err.println("Failed to show next: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }

    /**
     * Resends the zone menu message to the user based on the selected state.
     * @param chatId The chat ID to send the message to
     * @param stateMessage The selected state message
     * @return The message ID of the sent message
     */
    public Integer resendZoneMenu(String chatId, String stateMessage) {
        SendMessage message = new SendMessage();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (!(stateMessage.equals("Pulau Pinang") || stateMessage.equals("Melaka"))) {
            State state = new State();
            String[][] buttons;

            switch (stateMessage) {
                case "Johor":
                    buttons = State.Johor();
                    break;
                case "Kedah":
                    buttons = State.Kedah();
                    break;
                case "Kelantan":
                    buttons = State.Kelantan();
                    break;
                case "Negeri Sembilan":
                    buttons = State.NegeriSembilan();
                    break;
                case "Pahang":
                    buttons = State.Pahang();
                    break;
                case "Perlis":
                    buttons = State.Perlis();
                    break;
                case "Perak":
                    buttons = State.Perak();
                    break;
                case "Sabah":
                    buttons = State.Sabah();
                    break;
                case "Selangor":
                    buttons = State.Selangor();
                    break;
                case "Sarawak":
                    buttons = State.Sarawak();
                    break;
                case "Terengganu":
                    buttons = State.Terengganu();
                    break;
                default:
                    buttons = State.WilayahPersekutuan();
                    break;
            }

            for (String[] buttonRow : buttons) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                for (String buttonText : buttonRow) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(buttonText);
                    button.setCallbackData(buttonText);
                    row.add(button);
                }
                keyboard.add(row);
            }
        }
        else {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            if(stateMessage.equals("Melaka")) {
                button.setText(Language.zonMelaka(chatId));
                button.setCallbackData("melak");
            }
            else {
                button.setText(Language.zonPenang(chatId));
                button.setCallbackData("penang");
            }
            row.add(button);
            keyboard.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        message.setChatId(chatId);
        message.setText(Language.zone(chatId));
        message.setReplyMarkup(markup);

        try {
            // Send the message and get the response
            Message sentMessage = bot.execute(message);

            // Return the message ID
            return sentMessage.getMessageId();
        } catch (Exception e) {
            System.err.println("Failed to show zone: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }

    /**
     * Resends the state menu message to the user.
     * @param chatId The chat ID to send the message to
     * @param textMessage The text message to send
     * @return The message ID of the sent message
     */
    public Integer resendStateMenu(String chatId, String textMessage) {
        SendMessage message = new SendMessage();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        String[][] buttons = {
                {"Johor", "Kedah"},
                {"Kelantan", "Melaka"},
                {"Negeri Sembilan", "Pahang"},
                {"Perlis", "Pulau Pinang"},
                {"Perak", "Sabah"},
                {"Selangor", "Sarawak"},
                {"Terengganu", "Wilayah Persekutuan"}
        };

        for (String[] buttonRow : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String buttonText : buttonRow) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonText);
                button.setCallbackData(buttonText);
                row.add(button);
            }
            keyboard.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        // Create the message
        message.setChatId(chatId);
        message.setText(textMessage);
        message.setReplyMarkup(markup);

        try {
            // Send the message and get the response
            Message sentMessage = bot.execute(message);

            // Return the message ID
            return sentMessage.getMessageId();
        } catch (Exception e) {
            System.err.println("Failed to show state: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }

    /**
     * Resends the prayer times message to the user.
     * @param waktuSolat The prayer times information
     * @param chatId The chat ID to send the message to
     */
    public void reSendwaktuSolatToTelegram(String waktuSolat, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setParseMode("MarkdownV2");
        if (waktuSolat != null) {
            message.setText(waktuSolat);
        } else {
            message.setText(Language.failedPrayerMessage(chatId));
        }

        try {
            bot.execute(message);
        } catch (TelegramApiRequestException e) {
            System.err.println("Failed to send message: " + e.getApiResponse());
        } catch (TelegramApiException e) {
            System.err.println("Failed to send prayer times message: " + e.getMessage());
        }
    }
}