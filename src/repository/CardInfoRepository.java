package repository;

import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CardInfoRepository {
    private final Statement st;
    private String query;
    private final String folderLocation = Env.getFolderLocation();

    public CardInfoRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS CardInfo;\n"+
                "CREATE TABLE CardInfo (\n" +
                "  cardID integer,\n" +
                "  cardName VARCHAR(50),\n" +
                "  monthlyUseCount integer,\n" +
                "  Primary KEY(cardID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void importCSV() throws SQLException {
//        절대경로로 개인마다 바꿔야함.
//        경로에 한글이 껴있으면 안됨.
        query = "copy CardInfo FROM '" + folderLocation + "\\cardInfo.csv' DELIMITER ',' CSV HEADER;";
//        query = "copy CardInfo FROM '/private/tmp/cardInfo.csv' DELIMITER ',' CSV HEADER;";
        st.executeUpdate(query);

    }

    public void showTable() throws SQLException {
        query = "select * from CardInfo";
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s", "cardID", "cardName", "monthlyUseCount");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3));
            System.out.println();
        }
    }
}
