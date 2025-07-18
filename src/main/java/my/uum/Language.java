package my.uum;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class for managing language-specific messages in the Telegram bot.
 * Provides methods to generate language-specific messages for various functionalities.
 * @author Aiman Norazli
 */
public class Language {
    static UserInformation ui = new UserInformation();


    /**
     * Generates the message prompting the user to choose a language.
     * @param chatId The chat ID of the user
     * @return The message prompting the user to choose a language
     */
    public static String chooseLanguage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Sila pilih bahasa yang anda mahu gunakan.";
        }else {
            return "Please choose your preferred language."; // Default to English
        }
    }

    /**
     * Generates the menu message based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The menu message
     */
    public static String menuMessage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "*Selamat datang ke Menu Bot\\!*\n\n" +
                    "\\- /waktu\\_solat \\- Dapatkan waktu solat bagi kawasan anda\\.\n" +
                    "\\- /tukar\\_negeri \\- Tukar negeri anda\\.\n" +
                    "\\- /tukar\\_kawasan \\- Tukar kawasan anda\\.\n" +
                    "\\- /tukar\\_bahasa \\- Tukar bahasa yang digunakan\\.\n" +
                    "\\- /sumber \\- Papar sumber data bagi waktu solat yang digunakan bot ini\\.\n" +
                    "\\- /papar\\_menu \\- Papar Menu ini\n\n" +
                    "Untuk menggunakan arahan, taip nama arahan dengan tanda `/` di hadapannya \\(contohnya, `/help`\\)\\.";
        } else {
            return "*Welcome to the Bot Menu\\!*\n\n" +
                    "\\- /prayer\\_time \\- Get prayer time at your place\\.\n" +
                    "\\- /switch\\_state \\- Switch your state\\.\n" +
                    "\\- /switch\\_area \\- Switch your area\\.\n" +
                    "\\- /change\\_language \\- Change language use\\.\n" +
                    "\\- /source \\- Display data source for prayer time that use by this bot\\.\n" +
                    "\\- /show\\_menu \\- Display this Menu\\.\n\n" +
                    "To use a command, type the command name prefixed with `/` \\(e\\.g, `/help`\\)\\.";
        }
    }

    /**
     * Generates the message informing the user about the selected language.
     * @param chatId The chat ID of the user
     * @return The message informing the user about the selected language
     */
    public static String informLanguage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Anda telah memilih *Bahasa Melayu*\\.\n\n@s294214\\_bot kini akan berkomunikasi dengan anda dalam bahasa melayu\\.";
        }else {
            return "You have choose *English* language\\.\n\nThe @s294214\\_bot will now interact with you in english\\."; // Default to English
        }
    }

    /**
     * Generates the message prompting the user to choose a state.
     * @param chatId The chat ID of the user
     * @return The message prompting the user to choose a state
     */
    public static String state(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Sila pilih negeri yang anda berada sekarang.";
        }else {
            return "Choose the state that you are in."; // Default to English
        }
    }

    /**
     * Generates the message prompting the user to choose a zone.
     * @param chatId The chat ID of the user
     * @return The message prompting the user to choose a zone
     */
    public static String zone(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Sekarang, sila pilih kawasan yang berdekatan dengan anda.";
        }else {
            return "Now, choose the nearest area to you."; // Default to English
        }
    }

    /**
     * Generates the text for the "Next" button based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the "Next" button
     */
    public static String nextButton(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Seterusnya";
        }else {
            return "Next"; // Default to English
        }
    }

    /**
     * Generates the text for the zone of Pulau Pinang based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the zone of Pulau Pinang
     */
    public static String zonPenang(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Seluruh Kawasan Pulau Pinang";
        }else {
            return "The Whole Area of Pulau Pinang"; // Default to English
        }
    }

    /**
     * Generates the text for the zone of Melaka based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the zone of Melaka
     */
    public static String zonMelaka(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Seluruh Kawasan Melaka";
        }else {
            return "The Whole Area of Melaka"; // Default to English
        }
    }

    /**
     * Generates the source information message based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The source information message
     */
    public static String source(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Data bagi @s294214\\_bot ini diperoleh dari laman sesawang [e\\-solat](https://www.e-solat.gov.my/)\\.";
        }else {
            return "The data source for @s294214\\_bot is obtained from the [e\\-solat](https://www.e\\-solat.gov.my/) website\\."; // Default to English
        }
    }

    /**
     * Generates the header for the output based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The header for the output
     */
    public static String outputHeader(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "\uD83C\uDF1F *WAKTU SOLAT HARI INI* \uD83C\uDF1F\n\n";
        }else {
            return "\uD83C\uDF1F *TODAY'S PRAYER TIMES* \uD83C\uDF1F\n\n"; // Default to English
        }
    }

    /**
     * Generates the text for the day output based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the day output
     */
    public static String outputDay(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "*Hari*: ";
        }else {
            return "*Day*: "; // Default to English
        }
    }

    /**
     * Generates the text for the state output based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the state output
     */
    public static String outputState(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "*Negeri*: ";
        }else {
            return "*State*: "; // Default to English
        }
    }

    /**
     * Generates the text for the area output based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The text for the area output
     */
    public static String outputArea(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "*Kawasan*: ";
        }else {
            return "*Area*: "; // Default to English
        }
    }

    /**
     * Generates the failed prayer message based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The failed prayer message
     */
    public static String failedPrayerMessage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Gagal untuk mendapatkan waktu solat\\.\nSila cuba sebentar lagi\\.";
        }else {
            return "Failed to fetch prayer times\\.\nPlease try again later\\."; // Default to English
        }
    }

    /**
     * Generates the failed zone area message based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The failed zone area message
     */
    public static String failedZoneArea(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Gagal untuk mendapatkan kawasan.\nSila cuba sebentar lagi.";
        }else {
            return "Failed to fetch zone area.\nPlease try again later."; // Default to English
        }
    }

    /**
     * Generates the error message based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The error message
     */
    public static String errorMessage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Maaf, pilihan anda tidak sah.\nSila cuba lagi.";
        }else {
            return "I'm sorry, that is not a valid option.\nPlease try again."; // Default to English
        }
    }

    /**
     * Generates the message for showing the menu based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The message for showing the menu
     */
    public static String showMenuMessage(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Tekan /papar_menu untuk teruskan.";
        }else {
            return "Tap /show_menu to continue."; // Default to English
        }
    }

    /**
     * Generates the message prompting the user to choose a state first based on the user's language preference.
     * @param chatId The chat ID of the user
     * @return The message prompting the user to choose a state first
     */
    public static String chooseStateFirst(String chatId) {
        if (ui.getUserLanguagePreference(chatId).equals("Bahasa Melayu")) {
            return "Sila pilih negeri anda dahulu.";
        }else {
            return "Please choose your state first."; // Default to English
        }
    }
}

/**
 * The UserInformation class represents information about each user in a chat system.
 * It stores details such as chat ID, language preference, state, zone, and last message information.
 * @author Aiman Norazli
 */
class UserInformation {
    private String chatId; // To hold the chatId of each user
    private String language; // To hold the language of each user
    private String state; // To hold the state of each user
    private String currentState; // To hold the current state of each user
    private String zone; // To hold the zone of each user
    private String lastSendMessage; // To hold the last message sent time of each user
    private Integer lastButtonMessageId; // To hold the last sent button chatId of each user
    private static int totalUser = 1;

    // Define an ArrayList to hold user information for each user
    private static final List<UserInformation> userInformations = new ArrayList<>();

    // File for persistent storage
    private static final String USER_DATA_FILE = "user_data.txt";
    // Static block to load user data on class load
    static {
        loadUserData();
        System.out.println("Total User: " + userInformations.size());
    }
    // Save all user info to file (chatId, language, state, zone)
    private static void saveUserData() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(USER_DATA_FILE))) {
            for (UserInformation user : userInformations) {
                // Escape commas in fields if needed
                String chatId = user.chatId == null ? "" : user.chatId.replace(",", " ");
                String language = user.language == null ? "" : user.language.replace(",", " ");
                String state = user.state == null ? "" : user.state.replace(",", " ");
                String zone = user.zone == null ? "" : user.zone.replace(",", " ");
                writer.println(chatId + "," + language + "," + state + "," + zone);
            }
        } catch (Exception e) {
            System.err.println("Failed to save user data: " + e.getMessage());
        }
    }

    // Load all user info from file
    private static void loadUserData() {
        java.io.File file = new java.io.File(USER_DATA_FILE);
        if (!file.exists()) return;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    UserInformation user = new UserInformation(parts[0]);
                    user.setLanguage(parts[1]);
                    user.setState(parts[2]);
                    user.setZone(parts[3]);
                    userInformations.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }

    /**
     * Default constructor for UserInformation class.
     * Initializes the currentState field with "STATE_NORMAL".
     */
    public UserInformation() {
        this.currentState = "STATE_NORMAL"; // Default current state
    }

    /**
     * Constructor to initialize all fields for a new user.
     *
     * @param chatId The chatId of the user.
     */
    private UserInformation(String chatId) {
        this.chatId = chatId; // Use the passed chatId value
        this.language = "English"; // Default language
        this.state = null; // No default state
        this.currentState = "STATE_NORMAL"; // Default current state
        this.zone = null; // No default zone
        this.lastSendMessage = null; // No default last message time
        this.lastButtonMessageId = null; // No default last button
    }

    // Getter methods

    /**
     * Getter for chatId.
     *
     * @return The chatId of the user.
     */
    private String getChatId() {
        return chatId;
    }

    /**
     * Getter for language.
     *
     * @return The language preference of the user.
     */
    private String getLanguage() {
        return language;
    }

    /**
     * Getter for state.
     *
     * @return The state of the user.
     */
    private String getState() {
        return state;
    }

    /**
     * Getter for current state.
     *
     * @return The current state of the user.
     */
    private String getCurrentState() {
        return currentState;
    }

    /**
     * Getter for zone.
     *
     * @return The zone of the user.
     */
    private String getZone() {
        return zone;
    }

    /**
     * Getter for last sent message.
     *
     * @return The timestamp of the last message sent by the user.
     */
    private String getLastSendMessage() {
        return lastSendMessage;
    }

    /**
     * Getter for last button chatId.
     *
     * @return The ID of the last sent button message.
     */
    private Integer getLastButtonMessageId() {
        return lastButtonMessageId;
    }


    /**
     * Method to get the user language preference based on chatId.
     *
     * @param chatId The chatId of the user.
     * @return The language preference of the user. Default is "English" if not found.
     */
    public static String getUserLanguagePreference(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getLanguage();
            }
        }
        return "English";
    }

    /**
     * Method to get user state(state in malaysia) preference based on chatId.
     *
     * @param chatId The chatId of the user.
     * @return The state preference of the user. Returns null if not found.
     */
    public static String getUserState(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getState();
            }
        }
        return "null";
    }

    /**
     * Method to get user current state in telegram bot based on chatId.
     * If the user is not found, a new user with default values is created.
     *
     * @param chatId The chatId of the user.
     * @return The current state of the user.
     */
    public static String getUserCurrentState(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getCurrentState();
            }
        }

        // If the user is not found, create a new user with the given chatId and default values
        UserInformation newUser = new UserInformation(chatId);
        userInformations.add(newUser);
        System.out.println("Total User: " + userInformations.size());
        return newUser.getCurrentState(); // Return default current state
    }

    /**
     * Method to get user zone based on chatId.
     *
     * @param chatId The chatId of the user.
     * @return The zone of the user. Returns null if not found.
     */
    public static String getUserZone(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getZone();
            }
        }
        return "null";
    }

    /**
     * Method to get the timestamp of the last message sent by the user.
     *
     * @param chatId The chatId of the user.
     * @return The timestamp of the last message sent by the user. Returns null if not found.
     */
    public static String getLastSendMessage(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getLastSendMessage();
            }
        }
        return "null";
    }

    /**
     * Method to get the language preference code based on chatId.
     *
     * @param chatId The chatId of the user.
     * @return The language preference code of the user ("ms" for Bahasa Melayu, "en" for English).
     */
    public static String getUserLanguagePreferenceCode(String chatId) {
        String language = getUserLanguagePreference(chatId);
        if (language.equals("Bahasa Melayu")) {
            return "ms";
        } else {
            return "en";
        }
    }

    /**
     * Method to get the ID of the last sent button message based on chatId.
     *
     * @param chatId The chatId of the user.
     * @return The ID of the last sent button message. Returns null if not found.
     */
    public static Integer getLastButtonMessageId(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                return user.getLastButtonMessageId();
            }
        }
        return -1;
    }



    // Setter methods

    /**
     * Setter for language.
     *
     * @param language The language preference to set for the user.
     */
    private void setLanguage(String language) {
        if (language.equalsIgnoreCase("malay")) {
            this.language = "Bahasa Melayu";
        } else if (language.equalsIgnoreCase("inggeris")) {
            this.language = "English";
        } else {
            this.language = language; // Default case for unexpected language code
        }
    }

    /**
     * Setter for state.
     *
     * @param state The state to set for the user.
     */
    private void setState(String state) {
        this.state = state;
    }

    /**
     * Setter for current state.
     *
     * @param currentState The current state to set for the user.
     */
    private void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    /**
     * Setter for zone.
     *
     * @param zone The zone to set for the user.
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * Setter for last sent message.
     *
     * @param lastSendMessage The timestamp of the last message sent by the user.
     */
    private void setLastSendMessage(String lastSendMessage) {
        this.lastSendMessage = lastSendMessage;
    }

    /**
     * Setter for last button chatId.
     *
     * @param messageId The ID of the last sent button message.
     */
    private void setLastButtonMessageId(Integer messageId) {
        this.lastButtonMessageId = messageId;
    }

    /**
     * Method to set the current state of the user based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param currentState The current state to set for the user.
     */
    public static void setUserCurrentState(String chatId, String currentState) {
        for (UserInformation lang : userInformations) {
            if (lang.getChatId().equals(chatId)) {
                lang.setCurrentState(currentState);
                return;
            }
        }
    }

    /**
     * Method to set the language preference of the user based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param language The language preference to set for the user.
     */
    public static void setUserLanguagePreference(String chatId, String language) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                user.setLanguage(language);
                saveUserData();
                return;
            }
        }
        // Do not add a new entry; only update existing user
    }

    /**
     * Method to set the state of the user based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param state The state to set for the user.
     */
    public static void setUserState(String chatId, String state) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                user.setState(state);
                saveUserData();
                return;
            }
        }
        // Do not add a new entry; only update existing user
    }

    /**
     * Method to set the zone of the user based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param zone The zone to set for the user.
     */
    public static void setUserZone(String chatId, String zone) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                user.setZone(zone);
                saveUserData();
                return;
            }
        }
        // Do not add a new entry; only update existing user
    }

    /**
     * Method to set the timestamp of the last message sent by the user based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param lastSendMessage The timestamp of the last message sent by the user.
     */
    public static void setLastSendMessage(String chatId, String lastSendMessage) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                user.setLastSendMessage(lastSendMessage);
                return;
            }
        }
        // Do not add a new entry; only update existing user
    }

    /**
     * Method to set the ID of the last sent button message based on chatId.
     *
     * @param chatId The chatId of the user.
     * @param messageId The ID of the last sent button message.
     */
    public static void setLastButtonMessageId(String chatId, Integer messageId) {
        for (UserInformation user : userInformations) {
            if (user.getChatId().equals(chatId)) {
                user.setLastButtonMessageId(messageId);
                return;
            }
        }
        // Do not add a new entry; only update existing user
    }
}