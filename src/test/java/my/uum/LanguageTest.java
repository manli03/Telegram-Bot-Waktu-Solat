package my.uum;

import junit.framework.TestCase;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Ensures every language bundle defines the same message keys as the English default.
 */
public class LanguageTest extends TestCase {

    private static Properties load(String name) throws Exception {
        Properties p = new Properties();
        try (InputStreamReader r = new InputStreamReader(
                LanguageTest.class.getResourceAsStream("/" + name), StandardCharsets.UTF_8)) {
            p.load(r);
        }
        return p;
    }

    public void testMalayBundleHasSameKeysAsDefault() throws Exception {
        assertEquals(load("messages.properties").keySet(), load("messages_ms.properties").keySet());

        ResourceBundle.Control noFallback =
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        ResourceBundle en = ResourceBundle.getBundle("messages", Locale.ROOT, noFallback);
        ResourceBundle ms = ResourceBundle.getBundle("messages", Locale.forLanguageTag("ms"), noFallback);
        assertEquals("Seterusnya", ms.getString("nextButton"));
        assertTrue(en.getString("outputHeader").startsWith("🌟"));
        assertEquals("*Day*: ", en.getString("outputDay"));
    }
}
