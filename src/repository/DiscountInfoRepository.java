package repository;

import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DiscountInfoRepository {
    private Statement st;
    private ResultSet rs;
    private String query;
    private final String folderLocation = Env.getFolderLocation();

    public DiscountInfoRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS discountInfo;\n"+
                "CREATE TABLE discountInfo (\n" +
                "  cardID integer,\n" +
                "  cafeName VARCHAR(50),\n" +
                "  discountType varchar(1),\n" +
                "  discountValue float ,\n" +
                "  Primary KEY(cardID,cafeName)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void importCSV() throws SQLException {
//        절대경로로 개인마다 바꿔야함.
//        경로에 한글이 껴있으면 안됨.
        query = "COPY discountInfo FROM '" + folderLocation + "\\discountInfo.csv' DELIMITER ',' CSV HEADER;";
        st.executeUpdate(query);

    }

    public void showTable() throws SQLException {
        query = "select * from discountInfo";
        rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s%-20s", "cardID", "cafeName", "discountType", "discountValue");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            System.out.println();
        }
    }
}
