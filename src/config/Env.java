package config;

public class Env {
    private static String urlTaek = "jdbc:postgresql://localhost/postgres";
    private static String userTaek = "postgres";
    private static String pwdTaek = "1234";
    private static String folderLocation = "C:\\Users\\HYOTAEK\\Desktop";

    public static String getUrlTaek() {
        return urlTaek;
    }

    public static String getUserTaek() {
        return userTaek;
    }

    public static String getPwdTaek() {
        return pwdTaek;
    }
    public static String getFolderLocation() {
        return folderLocation;
    }
}
