package config;

public class Env {

//기윤
    private static String url = "jdbc:postgresql://127.0.0.1:5432/db_project";
    private static String user = "postgres";
    private static String pwd = "2390";
    private static String folderLocation = "/Users/crakel/Desktop/git_workspace/2021_DB_Project/data";



//지호
//    private static String url = "jdbc:postgresql://localhost/DB_project";
//    private static String user = "postgres";
//    private static String pwd = "qwerty1234";
//    private static String folderLocation = "C:\\Users\\Jiho\\DB";


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
