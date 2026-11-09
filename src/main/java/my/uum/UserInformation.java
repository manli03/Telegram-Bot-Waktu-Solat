package my.uum;

import org.telegram.telegrambots.meta.api.objects.User;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserInformation class represents information about each user in a chat system.
 * It stores details such as chat ID, username, language preference, state, zone, and last message information.
 * @author Aiman Norazli
 */
class UserInformation {
    private String chatId; // To hold the chatId of each user
    private String username; // To hold the username of each user
    private String language; // To hold the language of each user
    private String state; // To hold the state of each user
    private String currentState; // To hold the current state of each user
    private String zone; // To hold the zone of each user
    private String lastSendMessage; // To hold the last message sent time of each user
    private Integer lastButtonMessageId; // To hold the last sent button chatId of each user

    // Profile and activity log (persisted)
    private String firstName; // Telegram first name
    private String lastName; // Telegram last name
    private String telegramLanguage; // Language code of the user's Telegram app (e.g. "en", "ms")
    private boolean premium; // Whether the user has Telegram Premium
    private String chatType; // private, group, supergroup or channel
    private String firstSeen; // Time of the first interaction (ISO-8601, Malaysia time)
    private String lastSeen; // Time of the latest interaction (ISO-8601, Malaysia time)
    private long interactions; // Number of messages and button taps received
    private String lastAction; // Latest command, text or button data received

    // Define an ArrayList to hold user information for each user
    private static final List<UserInformation> userInformations = new ArrayList<>();

    // File for persistent storage (CSV with a header row)
    private static final Path USER_DATA_FILE = Paths.get(System.getenv().getOrDefault("USER_DATA_FILE", "user_data.txt"));
    // The first five columns match the original file format, so older files still load
    private static final String HEADER = "chatId,username,language,state,zone,firstName,lastName,"
            + "telegramLanguage,premium,chatType,firstSeen,lastSeen,interactions,lastAction";
    private static final ZoneId MALAYSIA = ZoneId.of("Asia/Kuala_Lumpur");

    // Static block to load user data on class load
    static {
        loadUserData();
        System.out.println("Total User: " + userInformations.size());
    }

    // Commas and line breaks would break the CSV, so replace them with spaces
    private static String csv(Object value) {
        return value == null ? "" : value.toString().replaceAll("[,\r\n]", " ");
    }

    // Save all user info to file
    private static void saveUserData() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(USER_DATA_FILE, StandardCharsets.UTF_8))) {
            writer.println(HEADER);
            for (UserInformation u : userInformations) {
                writer.println(String.join(",", csv(u.chatId), csv(u.username), csv(u.language), csv(u.state),
                        csv(u.zone), csv(u.firstName), csv(u.lastName), csv(u.telegramLanguage), csv(u.premium),
                        csv(u.chatType), csv(u.firstSeen), csv(u.lastSeen), csv(u.interactions), csv(u.lastAction)));
            }
        } catch (Exception e) {
            System.err.println("Failed to save user data: " + e.getMessage());
        }
    }

    // Load all user info from file
    private static void loadUserData() {
        if (!Files.exists(USER_DATA_FILE)) return;
        try (BufferedReader reader = Files.newBufferedReader(USER_DATA_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("chatId,")) continue; // header row
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                UserInformation user = new UserInformation(parts[0]);
                user.setUsername(parts[1]);
                user.setLanguage(parts[2]);
                user.setState(parts[3]);
                user.setZone(parts[4]);
                if (parts.length >= 14) {
                    user.firstName = parts[5];
                    user.lastName = parts[6];
                    user.telegramLanguage = parts[7];
                    user.premium = Boolean.parseBoolean(parts[8]);
                    user.chatType = parts[9];
                    user.firstSeen = parts[10];
                    user.lastSeen = parts[11];
                    user.interactions = parts[12].isEmpty() ? 0 : Long.parseLong(parts[12]);
                    user.lastAction = parts[13];
                }
                userInformations.add(user);
            }
        } catch (Exception e) {
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }

    /**
     * Records an interaction: refreshes the user's Telegram profile, updates first/last seen,
     * increments the interaction count and stores the last action. Creates the user if new.
     *
     * @param chatId   The chatId of the user.
     * @param from     The Telegram user who sent the update.
     * @param chatType The chat type (private, group, ...), or null to keep the stored one.
     * @param action   The text or button data received.
     */
    public static void recordActivity(String chatId, User from, String chatType, String action) {
        String now = ZonedDateTime.now(MALAYSIA).truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        UserInformation user = find(chatId);
        if (user == null) {
            user = new UserInformation(chatId);
            user.firstSeen = now; // Left empty for users saved before this column existed (unknown)
            userInformations.add(user);
            System.out.println("Total User: " + userInformations.size());
        }
        user.lastSeen = now;
        user.interactions++;
        user.lastAction = action;
        if (chatType != null) {
            user.chatType = chatType;
        }
        if (from != null) {
            user.username = from.getUserName();
            user.firstName = from.getFirstName();
            user.lastName = from.getLastName();
            user.telegramLanguage = from.getLanguageCode();
            user.premium = Boolean.TRUE.equals(from.getIsPremium());
        }
        saveUserData();
    }

    private static UserInformation find(String chatId) {
        for (UserInformation user : userInformations) {
            if (user.chatId.equals(chatId)) {
                return user;
            }
        }
        return null;
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
        this.username = null; // No default username
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
     * Setter for username.
     *
     * @param username The latest username of the user.
     */
    private void setUsername(String username) {
        this.username = username;
    }

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
    private void setZone(String zone) {
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