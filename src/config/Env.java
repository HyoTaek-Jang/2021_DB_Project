package config;

public class Env {
    private static String urlTaek = "jdbc:postgresql://localhost/postgres";
    private static String userTaek = "postgres";
    private static String pwdTaek = "1234";

    public static String getUrlTaek() {
        return urlTaek;
    }

    public static String getUserTaek() {
        return userTaek;
    }

    public static String getPwdTaek() {
        return pwdTaek;
    }
}
