package my.uum;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The WaktuSolatAPI class provides methods to retrieve prayer times for a specified chat ID.
 * It utilizes the e-solat API to obtain accurate prayer times based on the user's location.
 * @author Aiman Norazli
 */
public class WaktuSolatAPI {

    /**
     * Retrieves the prayer times for a specified chat ID.
     *
     * @param chatId The ID of the chat to retrieve prayer times for.
     * @return A formatted string containing the prayer times for the specified chat ID, or null if an error occurs.
     */
    public static String getPrayerTimes(String chatId) {

        String area;
        try {
            // Determine zone code based on area
            area = UserInformation.getUserZone(chatId);
            if (area == null)
                throw new Exception();
        } catch (Exception e) {
            return null;
        }
        String zoneCode = Location.getZoneCode(area);

        // Rename certain area
        if(area.equals("melak"))
            area = Language.zonMelaka(chatId);
        else if(area.equals("penang"))
            area = Language.zonPenang(chatId);

        // Set Malaysia's timezone
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDate todayMalaysia = ZonedDateTime.now(malaysiaZone).toLocalDate();

        // Define OkHttpClient and request URL
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.e-solat.gov.my/index.php?r=esolatApi/takwimsolat&period=today&zone=" + zoneCode;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            // Execute API request and obtain response
            String jsonData;
            try (Response response = client.newCall(request).execute()) {
                assert response.body() != null;
                jsonData = response.body().string();
            }

            // Parse JSON data using Gson
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

            // Extract serverTime object
//            String serverTime = jsonObject.get("serverTime").getAsString();
//            String dateValue = serverTime.split(" ")[0];

            // Validate date consistency
//            if (!todayMalaysia.toString().equals(dateValue)) {
//                System.err.println("API returned a different date: " + dateValue);
//            }

            // Extract prayerTime array
            JsonArray prayerTimeArray = jsonObject.getAsJsonArray("prayerTime");
            if (prayerTimeArray != null && !prayerTimeArray.isEmpty()) {
                // Access the first element in the prayerTime array (assuming only one element)
                JsonObject prayerTimeObject = prayerTimeArray.get(0).getAsJsonObject();

                // Create a StringBuilder to build the formatted output
                StringBuilder prayerTimes = new StringBuilder();

                // Define a time formatter for 12-hour format
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                String negeri = UserInformation.getUserState(chatId);
                String negeriFormat = Location.toTitleCase(negeri);
                String areaFormat = Location.toTitleCase(area);

                // Access the current date
                LocalDate currentDate = LocalDate.now();
//                LocalDate currentDate = LocalDate.parse(dateValue); // Use date from api server
                String localLanguage = UserInformation.getUserLanguagePreferenceCode(chatId);
                Locale malaysiaLocale = new Locale(localLanguage, "MY"); //Change language
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", malaysiaLocale);
                String dateAccessedFormatted = currentDate.format(dateFormatter);

                // Extract and format each prayer time
                String imsak = LocalTime.parse(prayerTimeObject.get("imsak").getAsString()).format(timeFormatter).toLowerCase();
                String fajr = LocalTime.parse(prayerTimeObject.get("fajr").getAsString()).format(timeFormatter).toLowerCase();
                String syuruk = LocalTime.parse(prayerTimeObject.get("syuruk").getAsString()).format(timeFormatter).toLowerCase();
                String dhuhr = LocalTime.parse(prayerTimeObject.get("dhuhr").getAsString()).format(timeFormatter).toLowerCase();
                String dhuha = LocalTime.parse(prayerTimeObject.get("dhuha").getAsString()).format(timeFormatter).toLowerCase();
                String asr = LocalTime.parse(prayerTimeObject.get("asr").getAsString()).format(timeFormatter).toLowerCase();
                String maghrib = LocalTime.parse(prayerTimeObject.get("maghrib").getAsString()).format(timeFormatter).toLowerCase();
                String isha = LocalTime.parse(prayerTimeObject.get("isha").getAsString()).format(timeFormatter).toLowerCase();

                // Append header of the output
                prayerTimes.append(Language.outputHeader(chatId))
                        .append(Language.outputDay(chatId)).append(dateAccessedFormatted).append("\n")
                        .append(Language.outputState(chatId)).append(negeriFormat).append("\n")
                        .append(Language.outputArea(chatId)).append(areaFormat).append("\n\n");

                // Append the formatted prayer times to the output
                prayerTimes.append("*Imsak*: ").append(imsak).append("\n")
                        .append("*Subuh*: ").append(fajr).append("\n")
                        .append("*Syuruk*: ").append(syuruk).append("\n")
                        .append("*Dhuha*: ").append(dhuha).append("\n")
                        .append("*Zohor*: ").append(dhuhr).append("\n")
                        .append("*Asar*: ").append(asr).append("\n")
                        .append("*Maghrib*: ").append(maghrib).append("\n")
                        .append("*Isyak*: ").append(isha).append("\n");

                return prayerTimes.toString();
            }
        } catch (Exception e) {
            System.err.println("An error occurred while parsing JSON data: " + e.getMessage());
            System.out.println("Failed to get " + area);
        }
        return null;
    }
}
