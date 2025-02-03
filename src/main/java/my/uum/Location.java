package my.uum;

/**
 * The Location class provides utility methods for location-related operations.
 * @author Aiman Norazli
 */
public class Location {

    /**
     * Converts a given string to title case.
     *
     * @param givenString The string to convert to title case.
     * @return The string converted to title case for each word.
     */
    public static String toTitleCase(String givenString) {
        // Escape open and close brackets by inserting a backslash before them
        givenString = givenString.replace("(", "\\(");
        givenString = givenString.replace(")", "\\)");

        // Split the string into words
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        // Iterate through each word, convert the first character to uppercase,
        // and append the rest of the word
        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }

        // Return the result after removing trailing whitespace
        return sb.toString().trim();
    }

    /**
     * Checks if a given state is valid.
     *
     * @param state The state to check.
     * @return true if the state is valid, false otherwise.
     */
    public static boolean checkState (String state) {
        // Define the data
        boolean check = false;

        String[] states = {"Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", "Pahang", "Perak", "Perlis",
                "Pulau Pinang", "Sabah", "Sarawak", "Selangor", "Terengganu", "Wilayah Persekutuan"};

        for (String s : states) {
            if (state.equals(s)) {
                check = true;
                break;
            }
        }
        return check;
    }

    /**
     * Checks if a given location is valid.
     *
     * @param location The location to check.
     * @return true if the location is valid, false otherwise.
     */
    public static boolean checkLocation (String location) {
        // Define the data
        boolean check = false;
        String[][] statesAndZones = {
                {"Johor", "Pulau Aur", "Pulau Pemanggil", "Johor Bahru", "Kota Tinggi", "Mersing", "Kulai", "Kluang", "Pontian", "Batu Pahat", "Muar", "Segamat", "Gemas Johor", "Tangkak"},
                {"Kedah", "Kota Setar", "Kubang Pasu", "Pokok Sena (Daerah Kecil)", "Kuala Muda", "Yan", "Pendang", "Padang Terap", "Sik", "Baling", "Bandar Baharu", "Kulima", "Langkawi", "Puncak Gunung Jerai"},
                {"Kelantan", "Bachok", "Kota Bharu", "Machang", "Pasir Mas", "Pasir Puteh", "Tanah Merah", "Tumpat", "Kuala Krai", "Mukim Chiku", "Gua Musang (Daerah Galas Dan Bertam)", "Jeli", "Jajahan Kecil Lojing"},
                {"Melaka", "melak"},
                {"Negeri Sembilan", "Tampin", "Jempol", "Jelebu", "Kuala Pilah", "Rembau", "Port Dickson", "Seremban"},
                {"Pahang", "Pulau Tioman", "Kuantan", "Pekan", "Rompin", "Muadzam Shah", "Jerantut", "Temerloh", "Maran", "Bera", "Chenor", "Jengka", "Bentong", "Lipis", "Raub", "Genting Sempah", "Janda Baik", "Bukit Tinggi", "Cameron Highlands", "Genting Higlands", "Bukit Fraser"},
                {"Perak", "Tapah", "Slim River", "Tanjung Malim", "Kuala Kangsar", "Sg. Siput", "Ipoh", "Batu Gajah", "Kampar", "Lenggong", "Pengkalan Hulu", "Grik", "Temengor", "Belum", "Kg Gajah", "Teluk Intan", "Bagan Datuk", "Seri Iskandar", "Beruas", "Parit", "Lumut", "Sitiawan", "Pulau Pangkor", "Selama", "Taiping", "Bagan Serai", "Parit Buntar", "Bukit Larut"},
                {"Perlis", "Kangar", "Padang Besar", "Arau"},
                {"Pulau Pinang", "penang"},
                {"Sabah", "Bahagian Sandakan (Timur)", "Bukit Garam", "Semawang", "Temanggong", "Tambisan", "Bandar Sandakan", "Sukau", "Beluran", "Telupid", "Pinangah", "Terusan", "Kuamut", "Bahagian Sandakan (Barat)", "Lahad Datu", "Silabukan", "Kunak", "Sahabat", "Semporna", "Tungku", "Bahagian Tawau (Timur)", "Bandar Tawau", "Balong", "Merotai", "Kalabakan", "Bahagian Tawau (Barat)", "Kudat", "Kota Marudu", "Pitas", "Pulau Banggi", "Bahagian Kudat", "Gunung Kinabalu", "Kota Kinabalu", "Ranau", "Kota Belud", "Tuaran", "Penampang", "Papar", "Putatan", "Bahagian Pantai Barat", "Pensiangan", "Keningau", "Tambunan", "Nabawan", "Bahagian Pendalaman (Atas)", "Beaufort", "Kuala Penyu", "Sipitang", "Tenom", "Long Pasia", "Membakut", "Weston", "Bahagian Pendalaman (Bawah)"},
                {"Sarawak", "Limbang", "Lawas", "Sundar", "Trusan", "Miri", "Niah", "Bekenu", "Sibuti", "Marudi", "Pandan", "Belaga", "Suai", "Tatau", "Sebauh", "Bintulu", "Sibu", "Mukah", "Dalat", "Song", "Igan", "Oya", "Balingian", "Kanowit", "Kapit", "Sarikei", "Matu", "Julau", "Rajang", "Daro", "Bintangor", "Belawai", "Lubok Antu", "Sri Aman", "Roban", "Debak", "Kabong", "Lingga", "Engkelili", "Betong", "Spaoh", "Pusa", "Saratok", "Serian", "Simunjan", "Samarahan", "Sebuyau", "Meludam", "Kuching", "Bau", "Lundu", "Sematan", "Khas (Kampung Patarikan)"},
                {"Selangor", "Gombak", "Petaling", "Sepang", "Hulu Langat", "Hulu Selangor", "Shah Alam", "Kuala Selangor", "Sabak Bernam", "Klang", "Kuala Langat"},
                {"Terengganu", "Kuala Terengganu", "Marang", "Kuala Nerus", "Besut", "Setiu", "Hulu Terengganu", "Dungun", "Kemaman"},
                {"Wilayah Persekutuan", "Kuala Lumpur", "Putrajaya", "Labuan"}
        };


        // Find the data
        for (String[] zone : statesAndZones) {
            for (int i = 1; i < zone.length; i++) {
                if (location.equals(zone[i])) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    /**
     * Gets the zone code for a given area.
     *
     * @param area The area to get the zone code for.
     * @return The zone code for the given area.
     */
    public static String getZoneCode(String area) {

        // Use a switch to map area names to zone codes
        switch (area) {
            case "Pulau Aur":
            case "Pulau Pemanggil":
                return "JHR01";
            case "Johor Bahru":
            case "Kota Tinggi":
            case "Mersing":
                return "JHR02";
            case "Kluang":
            case "Pontian":
                return "JHR03";
            case "Batu Pahat":
            case "Muar":
            case "Segamat":
            case "Gemas Johor":
                return "JHR04";
            case "Kota Setar":
            case "Kubang Pasu":
            case "Pokok Sena":
                return "KDH01";
            case "Kuala Muda":
            case "Yan":
            case "Pendang":
                return "KDH02";
            case "Padang Terap":
            case "Sik":
                return "KDH03";
            case "Baling":
                return "KDH04";
            case "Bandar Baharu":
            case "Kulim":
                return "KDH05";
            case "Langkawi":
                return "KDH06";
            case "Gunung Jerai":
                return "KDH07";
            case "Bachok":
            case "Kota Bharu":
            case "Machang":
            case "Pasir Mas":
            case "Pasir Puteh":
            case "Tanah Merah":
            case "Tumpat":
            case "Kuala Krai":
            case "Mukim Chiku":
                return "KTN01";
            case "melak":
                return "MLK01";
            case "Gua Musang (Daerah Galas Dan Bertam)":
            case "Jeli":
            case "Jajahan Kecil Lojing":
                return "KTN02";
            case "Tampin":
            case "Jempol":
                return "NGS01";
            case "Jelebu":
            case "Kuala Pilah":
            case "Port Dickson":
            case "Rembau":
            case "Seremban":
                return "NGS02";
            case "Pulau Tioman":
                return "PHG01";
            case "Kuantan":
            case "Pekan":
            case "Rompin":
            case "Muadzam Shah":
                return "PHG02";
            case "Jerantut":
            case "Temerloh":
            case "Maran":
            case "Bera":
            case "Chenor":
            case "Jengka":
                return "PHG03";
            case "Bentong":
            case "Lipis":
            case "Raub":
                return "PHG04";
            case "Genting Sempah":
            case "Janda Baik":
            case "Bukit Tinggi":
                return "PHG05";
            case "Cameron Highlands":
            case "Genting Highlands":
            case "Bukit Fraser":
                return "PHG06";
            case "Kangar":
            case "Padang Besar":
            case "Arau":
                return "PLS01";
            case "penang":
                return "PNG01";
            case "Tapah":
            case "Slim River":
            case "Tanjung Malim":
                return "PRK01";
            case "Kuala Kangsar":
            case "Sg. Siput":
            case "Ipoh":
            case "Batu Gajah":
            case "Kampar":
                return "PRK02";
            case "Lenggong":
            case "Pengkalan Hulu":
            case "Grik":
                return "PRK03";
            case "Temengor":
            case "Belum":
                return "PRK04";
            case "Kg Gajah":
            case "Teluk Intan":
            case "Bagan Datuk":
            case "Seri Iskandar":
            case "Beruas":
            case "Parit":
            case "Lumut":
            case "Sitiawan":
            case "Pulau Pangkor":
                return "PRK05";
            case "Selama":
            case "Taiping":
            case "Bagan Serai":
            case "Parit Buntar":
                return "PRK06";
            case "Bukit Larut":
                return "PRK07";
            case "Bahagian Sandakan (Timur)":
            case "Bukit Garam":
            case "Semawang":
            case "Temanggong":
            case "Tambisan":
            case "Bandar Sandakan":
                return "SBH01";
            case "Beluran":
            case "Telupid":
            case "Pinangah":
            case "Terusan":
            case "Kuamut":
            case "Bahagian Sandakan (Barat)":
                return "SBH02";
            case "Lahad Datu":
            case "Silabukan":
            case "Kunak":
            case "Sahabat":
            case "Semporna":
            case "Tungku":
            case "Bahagian Tawau (Timur)":
                return "SBH03";
            case "Bandar Tawau":
            case "Balong":
            case "Merotai":
            case "Kalabakan":
            case "Bahagian Tawau (Barat)":
                return "SBH04";
            case "Kudat":
            case "Kota Marudu":
            case "Pitas":
            case "Pulau Banggi":
            case "Bahagian Kudat":
                return "SBH05";
            case "Gunung Kinabalu":
                return "SBH06";
            case "Kota Kinabalu":
            case "Ranau":
            case "Kota Belud":
            case "Tuaran":
            case "Penampang":
            case "Papar":
            case "Putatan":
            case "Bahagian Pantai Barat":
                return "SBH07";
            case "Pensiangan":
            case "Keningau":
            case "Tambunan":
            case "Nabawan":
            case "Bahagian Pendalaman (Atas)":
                return "SBH08";
            case "Beaufort":
            case "Kuala Penyu":
            case "Sipitang":
            case "Tenom":
            case "Long Pa Sia":
            case "Membakut":
            case "Weston":
            case "Bahagian Pendalaman (Bawah)":
                return "SBH09";
            case "Gombak":
            case "Petaling":
            case "Sepang":
            case "Hulu Langat":
            case "Hulu Selangor":
            case "Rawang":
            case "Shah Alam":
                return "SGR01";
            case "Kuala Selangor":
            case "Sabak Bernam":
                return "SGR02";
            case "Klang":
            case "Kuala Langat":
                return "SGR03";
            case "Limbang":
            case "Lawas":
            case "Sundar":
            case "Trusan":
                return "SWK01";
            case "Miri":
            case "Niah":
            case "Bekenu":
            case "Sibuti":
            case "Marudi":
                return "SWK02";
            case "Pandan":
            case "Belaga":
            case "Suai":
            case "Tatau":
            case "Sebauh":
            case "Bintulu":
                return "SWK03";
            case "Sibu":
            case "Mukah":
            case "Dalat":
            case "Song":
            case "Igan":
            case "Oya":
            case "Balingian":
            case "Kanowit":
            case "Kapit":
                return "SWK04";
            case "Sarikei":
            case "Matu":
            case "Julau":
            case "Rajang":
            case "Daro":
            case "Bintangor":
            case "Belawai":
                return "SWK05";
            case "Lubok Antu":
            case "Sri Aman":
            case "Roban":
            case "Debak":
            case "Kabong":
            case "Lingga":
            case "Engkelili":
            case "Betong":
            case "Spaoh":
            case "Pusa":
            case "Saratok":
                return "SWK06";
            case "Serian":
            case "Simunjan":
            case "Samarahan":
            case "Sebuyau":
            case "Meludam":
                return "SWK07";
            case "Kuching":
            case "Bau":
            case "Lundu":
            case "Sematan":
                return "SWK08";
            case "Zon Khas (Kampung Patarikan)":
                return "SWK09";
            case "Kuala Terengganu":
            case "Marang":
            case "Kuala Nerus":
                return "TRG01";
            case "Besut":
            case "Setiu":
                return "TRG02";
            case "Hulu Terengganu":
                return "TRG03";
            case "Dungun":
            case "Kemaman":
                return "TRG04";
            case "Kuala Lumpur":
            case "Putrajaya":
                return "WLY01";
            case "Labuan":
                return "WLY02";
        }
        // Return null if no match is found
        return "null";
    }
}