package config;

import java.sql.*;

public class SetUpDB {

    public static Statement setUpDB() throws SQLException {

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

//      사용자 url, id, pwd 맞게 설정
        String url = Env.getUrlTaek();
        String user = Env.getUserTaek();
        String password = Env.getPwdTaek();

        try {
            conn = DriverManager.getConnection(url, user, password);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next())
                System.out.println(rs.getString(1));
            System.out.println("Success Connecting");
        } catch (SQLException sqlEX) {
            throw sqlEX;
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException sqlEX) {
                throw sqlEX;
            }
            return st;
        }
    }


}
