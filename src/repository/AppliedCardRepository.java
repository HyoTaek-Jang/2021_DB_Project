package repository;

//import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppliedCardRepository {
    private final Statement st;
    private String query;
    //private final String folderLocation = Env.getFolderLocation();

    public AppliedCardRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS AppliedCard;\n" +
                "CREATE TABLE AppliedCard (\n" +
                "\tuID integer,\n" +
                "    cardID integer,\n" +
                "    useRemain integer,\n" +
                "    Primary KEY(uID,cardID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void insertAppliedCard() throws SQLException {  // 유저id와 카드id를 넣어줌
        query = "INSERT INTO AppliedCard VALUES (123,17);\n" +
                "INSERT INTO AppliedCard VALUES (123,5);\n" +
                "INSERT INTO AppliedCard VALUES (123,32);\n" +
                "INSERT INTO AppliedCard VALUES (234,22);\n" +
                "INSERT INTO AppliedCard VALUES (234,35);\n" +
                "INSERT INTO AppliedCard VALUES (345,75);\n" +
                "INSERT INTO AppliedCard VALUES (456,44);\n" +
                "INSERT INTO AppliedCard VALUES (456,64);\n" +
                "INSERT INTO AppliedCard VALUES (567,32);\n" +
                "INSERT INTO AppliedCard VALUES (678,17);";

        st.executeUpdate(query);

        //카드id로 그 카드의 monthlyUseCount를 찾아서 업데이트
        query = "UPDATE AppliedCard \n" +
                "SET useRemain = (SELECT monthlyUseCount FROM CardInfo WHERE AppliedCard.cardID = CardInfo.cardID);\n";

        st.executeUpdate(query);
    }




    public void showTable() throws SQLException {
        query = "select * from AppliedCard ORDER BY uID";
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-10s%-12s%-20s", "uID", "cardID", "useRemain");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-10s%-12s%-20s", rs.getString(1), rs.getString(2), rs.getString(3));
            System.out.println();
        }
    }
}
