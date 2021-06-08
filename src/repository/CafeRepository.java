package repository;

import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CafeRepository {
    private final Statement st;
    private String query;
    private final String folderLocation = Env.getFolderLocation();

    public CafeRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS Cafe;\n"+
                "CREATE TABLE Cafe (\n" +
                "  cafeID integer,\n" +
                "  cafeName VARCHAR(50),\n" +
                "  branchName VARCHAR(50),\n" +
                "  numberAddress VARCHAR(90),\n" +
                "  roadNameAddress VARCHAR(90),\n" +
                "  longitude float,\n" +
                "  latitude float,\n" +
                "  Primary KEY(cafeID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void importCSV() throws SQLException {
//        절대경로로 개인마다 바꿔야함.
//        경로에 한글이 껴있으면 안됨.
//        query = "\\copy Cafe FROM '" + folderLocation + "/cafeData.csv' DELIMITER ',' CSV HEADER;";
        query = "copy Cafe FROM '/private/tmp/cafeData.csv' DELIMITER ',' CSV HEADER;";
        st.executeUpdate(query);

    }

    public void showTable() throws SQLException {
        query = "select * from cafe";
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s%-20s%-20s", "cafeId", "cafeName", "branchName", "longitude", "latitude");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(6), rs.getString(7));
            System.out.println();
        }
    }


}
