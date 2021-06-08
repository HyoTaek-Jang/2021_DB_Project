package repository;

import config.Env;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DiscountInfoRepository {
    class DiscountInfo {
        private String cafeName;
        private String cardId;
        private String discountType;
        private String discountValue;

        public DiscountInfo(String cafeName, String cardId, String discountType, String discountValue) {
            this.cafeName = cafeName;
            this.cardId = cardId;
            this.discountType = discountType;
            this.discountValue = discountValue;
        }

        public String getCafeName() {
            return cafeName;
        }

        public String getCardId() {
            return cardId;
        }

        public String getDiscountType() {
            return discountType;
        }

        public String getDiscountValue() {
            return discountValue;
        }
    }

    private final Statement st;
    private String query;
    private final String folderLocation = Env.getFolderLocation();
    ArrayList<DiscountInfo> discountInfos = new ArrayList<>();


    public DiscountInfoRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS discountInfo;\n" +
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
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s%-20s", "cardID", "cafeName", "discountType", "discountValue");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            System.out.println();
        }
    }

    public void queryBrand(String brandName) throws SQLException {
        System.out.println(brandName + ", 해당 브랜드 할인 카드 안내입니다.");

        query = "SELECT cafeName, cardId, discounttype, discountvalue FROM discountInfo WHERE cafename LIKE '%' || '" + brandName + "' || '%';";
        ResultSet rs = st.executeQuery(query);
        boolean check = false;

        while (rs.next()) {
            check = true;
            discountInfos.add(new DiscountInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }

        if (check) queryDiscountInfo();

    }

    public void queryDiscountInfo() throws SQLException {
        for (DiscountInfo discountInfo : discountInfos) {

            query = "SELECT cardname, monthlyusecount FROM cardInfo WHERE cardId = " + discountInfo.getCardId() + ";";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                printBrandSearch(rs.getString(1), discountInfo.getDiscountType(), discountInfo.getDiscountValue(), rs.getString(2));
            }
        }

    }

    public void printBrandSearch(String cardName, String discountType, String discountValue, String monthlyUseCount) {
        String monUseCount = (Integer.parseInt(monthlyUseCount) > 50) ? "횟수 제한이 없습니다." : "월 " + monthlyUseCount + "회 사용 가능합니다.";
        String type = discountType.equals("p") ? "원 가격 할인 | " : "퍼센트 할인 | ";

        System.out.println();
        System.out.println(cardName + " | " + discountValue + type + monUseCount);
    }
}
