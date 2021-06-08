package repository;

import config.Env;
import config.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository {
    private final Statement st;
    private String query;

    public UserRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS Users;\n" +
                "CREATE TABLE Users (\n" +
                "  uID integer,\n" +
                "  longitude float,\n" +
                "  latitude float,\n" +
                "  Primary KEY(uID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void insertUser() throws SQLException {  // 유저id와 현재위치좌표 넣어줌
        query = "INSERT INTO Users VALUES (123, 127.05902969025047, 37.51207412593136);\n" +
                "INSERT INTO Users VALUES (234, 127.05902969025047, 37.51207412593136);\n" +
                "INSERT INTO Users VALUES (345, 127.05902969025047, 37.51207412593136);\n";

        st.executeUpdate(query);
    }

    public void queryLocation(int uID, double x, double y) throws SQLException {  // 유저id와 현재위치좌표 넣어줌
        query = "UPDATE Users SET longitude = " + x + ", latitude = " + y + "WHERE uID = " + uID;

        st.executeUpdate(query);
    }

    public void showTable() throws SQLException {
        query = "select * from Users ORDER BY uID";
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-10s%-30s%-30s", "uID", "longitude", "latitude");
        System.out.println("\n----------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-10s%-30s%-30s", rs.getString(1), rs.getString(2), rs.getString(3));
            System.out.println();
        }
    }
}
