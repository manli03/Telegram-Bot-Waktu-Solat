package my.uum;

/**
 * The State class provides methods to retrieve information of inline keyboard based on position and text on button
 * @author Aiman Norazli
 */
public class State {

    /**
     * Returns an array of zones in the state of Johor for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Johor
     */
    public static String[][] Johor() {
        return new String[][]{
                {"Pulau Aur", "Pulau Pemanggil"},
                {"Johor Bahru", "Kota Tinggi"},
                {"Mersing", "Kluang"},
                {"Pontian", "Batu Pahat"},
                {"Muar", "Segamat"},
                {"Gemas Johor"}
        };
    }

    /**
     * Returns an array of zones in the state of Kedah for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Kedah
     */
    public static String[][] Kedah() {
        return new String[][]{
                {"Kota Setar", "Kubang Pasu"},
                {"Kuala Muda", "Yan"},
                {"Pendang", "Padang Terap"},
                {"Sik", "Baling"},
                {"Bandar Baharu", "Kulim"},
                {"Langkawi"},
                {"Puncak Gunung Jerai"},
                {"Pokok Sena (Daerah Kecil)"}
        };

    }

    /**
     * Returns an array of zones in the state of Kelantan for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Kelantan
     */
    public static String[][] Kelantan() {
        return new String[][]{
                {"Bachok", "Kota Bharu"},
                {"Machang", "Pasir Mas"},
                {"Pasir Puteh", "Tanah Merah"},
                {"Tumpat", "Kuala Krai"},
                {"Mukim Chiku", "Jeli"},
                {"Jajahan Kecil Lojing"},
                {"Gua Musang (Daerah Galas Dan Bertam)"}
        };

    }

    /**
     * Returns an array of zones in the state of Negeri Sembilan for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Negeri Sembilan
     */
    public static String[][] NegeriSembilan() {
        return new String[][]{
                {"Tampin", "Jempol"},
                {"Jelebu", "Kuala Pilah"},
                {"Port Dickson", "Rembau"},
                {"Seremban"}
        };
    }

    /**
     * Returns an array of zones in the state of Pahang for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Pahang
     */
    public static String[][] Pahang() {
        return new String[][]{
                {"Pulau Tioman", "Kuantan"},
                {"Pekan", "Rompin"},
                {"Muadzam Shah", "Jerantut"},
                {"Temerloh", "Maran"},
                {"Bera", "Chenor"},
                {"Jengka", "Bentong"},
                {"Lipis", "Raub"},
                {"Genting Sempah", "Janda Baik"},
                {"Bukit Tinggi", "Cameron Highlands"},
                {"Genting Higlands", "Bukit Fraser"},
        };
    }

    /**
     * Returns an array of zones in the state of Perlis for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Perlis
     */
    public static String[][] Perlis() {
        return new String[][]{
                {"Kangar", "Padang Besar"},
                {"Arau"}
        };

    }

    /**
     * Returns an array of zones in the state of Perak for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Perak
     */
    public static String[][] Perak() {
        return new String[][]{
                {"Tapah", "Slim River"},
                {"Tanjung Malim", "Kuala Kangsar"},
                {"Sg. Siput", "Ipoh"},
                {"Batu Gajah", "Kampar"},
                {"Lenggong", "Pengkalan Hulu"},
                {"Grik", "Temengor"},
                {"Belum", "Kg Gajah"},
                {"Teluk Intan", "Bagan Datuk"},
                {"Seri Iskandar", "Beruas"},
                {"Parit", "Lumut"},
                {"Sitiawan", "Pulau Pangkor"},
                {"Selama", "Taiping"},
                {"Bagan Serai", "Parit Buntar"},
                {"Bukit Larut"}
        };
    }

    /**
     * Returns an array of zones in the state of Sabah for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Sabah
     */
    public static String[][] Sabah() {
        return new String[][]{
                {"Bukit Garam", "Semawang"},
                {"Temanggong", "Tambisan"},
                {"Bandar Sandakan", "Sukau"},
                {"Beluran", "Telupid"},
                {"Pinangah", "Terusan"},
                {"Kuamut", "Lahad Datu"},
                {"Silabukan", "Kunak"},
                {"Sahabat", "Semporna"},
                {"Tungku", "Bandar Tawau"},
                {"Balong", "Merotai"},
                {"Kalabakan", "Kudat"},
                {"Kota Marudu", "Pitas"},
                {"Pulau Banggi", "Bahagian Kudat"},
                {"Gunung Kinabalu", "Kota Kinabalu"},
                {"Ranau", "Kota Belud"},
                {"Tuaran", "Penampang"},
                {"Papar", "Putatan"},
                {"Bahagian Pantai Barat", "Pensiangan"},
                {"Keningau", "Tambunan"},
                {"Nabawan", "Beaufort"},
                {"Kuala Penyu", "Sipitang"},
                {"Tenom", "Long Pa Sia"},
                {"Membakut", "Weston"},
                {"Bahagian Sandakan (Timur)"},
                {"Bahagian Sandakan (Barat)"},
                {"Bahagian Tawau (Timur)"},
                {"Bahagian Tawau (Barat)"},
                {"Bahagian Pendalaman (Atas)"},
                {"Bahagian Pendalaman (Bawah)"}
        };
    }

    /**
     * Returns an array of zones in the state of Selangor for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Selangor
     */
    public static String[][] Selangor() {
        return new String[][]{
                {"Gombak", "Petaling"},
                {"Sepang", "Hulu Langat"},
                {"Hulu Selangor", "Shah Alam"},
                {"Kuala Selangor", "Sabak Bernam"},
                {"Klang", "Kuala Langat"}
        };
    }

    /**
     * Returns an array of zones in the state of Sarawak for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Sarawak
     */
    public static String[][] Sarawak() {
        return new String[][]{
                {"Limbang", "Lawas"},
                {"Sundar", "Trusan"},
                {"Miri", "Niah"},
                {"Bekenu", "Sibuti"},
                {"Marudi", "Pandan"},
                {"Belaga", "Suai"},
                {"Tatau", "Sebauh"},
                {"Bintulu", "Sibu"},
                {"Mukah", "Dalat"},
                {"Song", "Igan"},
                {"Oya", "Balingian"},
                {"Kanowit", "Kapit"},
                {"Sarikei", "Matu"},
                {"Julau", "Rajang"},
                {"Daro", "Bintangor"},
                {"Belawai", "Lubok Antu"},
                {"Sri Aman", "Roban"},
                {"Debak", "Kabong"},
                {"Lingga", "Engkelili"},
                {"Betong", "Spaoh"},
                {"Pusa", "Saratok"},
                {"Serian", "Simunjan"},
                {"Samarahan", "Sebuyau"},
                {"Meludam", "Kuching"},
                {"Bau", "Lundu"},
                {"Sematan"},
                {"Khas (Kampung Patarikan)"}
        };
    }

    /**
     * Returns an array of zones in the state of Terengganu for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in Terengganu
     */
    public static String[][] Terengganu() {
        return new String[][]{
                {"Kuala Terengganu", "Marang"},
                {"Kuala Nerus", "Besut"},
                {"Setiu", "Hulu Terengganu"},
                {"Dungun", "Kemaman"}
        };
    }

    /**
     * Returns an array of zones in the Federal Territories for use in inline keyboards.
     *
     * @return a 2D array of strings representing zones in the Federal Territories
     */
    public static String[][] WilayahPersekutuan() {
        return new String[][]{
                {"Kuala Lumpur", "Putrajaya"},
                {"Labuan"}
        };
    }
}