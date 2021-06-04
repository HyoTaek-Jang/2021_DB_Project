package config;

public class Env {
/*
//기윤
    private static String url = "jdbc:postgresql://localhost/postgres";
    private static String user = "postgres";
    private static String pwd = "1234";
    private static String folderLocation = "C:\\Users\\HYOTAEK\\Desktop";
*/

/*
//지호
    private static String url = "jdbc:postgresql://localhost/postgres";
    private static String user = "postgres";
    private static String pwd = "1234";
    private static String folderLocation = "C:\\Users\\HYOTAEK\\Desktop";
*/

// 택
    private static String url = "jdbc:postgresql://localhost/postgres";
    private static String user = "postgres";
    private static String pwd = "1234";
    private static String folderLocation = "C:\\Users\\HYOTAEK\\Desktop";

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPwd() {
        return pwd;
    }
    public static String getFolderLocation() {
        return folderLocation;
    }
}
