package my.uum;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Looks up user-facing messages from the {@code messages*.properties} resource bundles
 * ({@code messages.properties} = English default, {@code messages_ms.properties} = Bahasa Melayu).
 * To add a language, add {@code messages_<code>.properties} and map it in
 * {@link UserInformation#getUserLanguagePreferenceCode(String)}.
 * @author Aiman Norazli
 */
public final class Language {

    private static final String BUNDLE = "messages";
    // Never fall back to the JVM default locale: unknown languages resolve to the English base bundle.
    private static final ResourceBundle.Control NO_FALLBACK =
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    private Language() {
    }

    /**
     * Returns the message for {@code key} in the user's preferred language.
     * @param chatId The chat ID of the user
     * @param key    The message key in the properties files
     * @return The localized message
     */
    public static String get(String chatId, String key) {
        Locale locale = Locale.forLanguageTag(UserInformation.getUserLanguagePreferenceCode(chatId));
        return ResourceBundle.getBundle(BUNDLE, locale, NO_FALLBACK).getString(key);
    }

    /**
     * Returns the message for {@code key} with the {@code {bot}} placeholder replaced by the
     * bot username, escaped for Telegram MarkdownV2.
     * @param chatId      The chat ID of the user
     * @param key         The message key in the properties files
     * @param botUsername The bot username (without '@')
     * @return The localized message
     */
    public static String get(String chatId, String key, String botUsername) {
        return get(chatId, key).replace("{bot}", botUsername.replace("_", "\\_"));
    }
}
