package config;

import java.sql.*;

public class DB {
    private static Connection conn = null;
    private static Statement st = null;
    static ResultSet rs = null;

    public static Statement setUpDB() throws SQLException {

//      사용자 url, id, pwd 맞게 설정
        String url = Env.getUrl();
        String user = Env.getUser();
        String password = Env.getPwd();

        try {
            conn = DriverManager.getConnection(url, user, password);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next())
                System.out.println(rs.getString(1));
            System.out.println("Success Connecting");
        } catch (SQLException sqlEX) {
            throw sqlEX;
        }
        return st;
    }

    public static void closeDB() throws SQLException {
            try {
                rs.close();
                st.close();
                conn.close();

                System.out.println("Success DB disconnect");
            } catch (SQLException sqlEX) {
                throw sqlEX;
            }
    }
}
