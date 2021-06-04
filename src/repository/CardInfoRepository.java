package repository;

import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CardInfoRepository {
    private Statement st;
    private ResultSet rs;
    private String query;
    private final String folderLocation = Env.getFolderLocation();

    public CardInfoRepository(Statement st) {
        this.st = st;
    }

    public void createCafeTable() throws SQLException {
        query = "DROP TABLE IF EXISTS CardInfo;\n"+
                "CREATE TABLE CardInfo (\n" +
                "  cardID integer,\n" +
                "  cafeName VARCHAR(50),\n" +
                "  monthlyUseCount integer,\n" +
                "  Primary KEY(cardID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void importCSV() throws SQLException {
//        절대경로로 개인마다 바꿔야함.
//        경로에 한글이 껴있으면 안됨.
        query = "COPY CardInfo FROM '" + folderLocation + "\\cardInfo.csv' DELIMITER ',' CSV HEADER;";
        st.executeUpdate(query);

    }

    public void showTable() throws SQLException {
        query = "select * from cafe";
        rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s%-20s%-20s", "cafeId", "cafeName", "branchName", "longitude", "latitude");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(6), rs.getString(7));
            System.out.println();
        }
    }
}
