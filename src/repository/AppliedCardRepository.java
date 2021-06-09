package repository;

//import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AppliedCardRepository {
    private final Statement st;
    private String query;

    Scanner scan = new Scanner(System.in);
    //private final String folderLocation = Env.getFolderLocation();

    public AppliedCardRepository(Statement st) {
        this.st = st;
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS AppliedCard;\n" +
                "CREATE TABLE AppliedCard (\n" +
                "uID integer,\n" +
                "cardID integer,\n" +
                "useRemain integer,\n" +
                "Primary KEY(uID,cardID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void insertAppliedCard() throws SQLException {  // 유저id와 카드id를 넣어줌
        query = "INSERT INTO AppliedCard VALUES (123,17);\n" +
                "INSERT INTO AppliedCard VALUES (123,5);\n" +
                "INSERT INTO AppliedCard VALUES (123,32);\n" +
                "INSERT INTO AppliedCard VALUES (234,22);\n" +
                "INSERT INTO AppliedCard VALUES (234,35);\n" +
                "INSERT INTO AppliedCard VALUES (234,75);\n" +
                "INSERT INTO AppliedCard VALUES (234,44);\n" +
                "INSERT INTO AppliedCard VALUES (345,64);\n" +
                "INSERT INTO AppliedCard VALUES (345,32);\n" +
                "INSERT INTO AppliedCard VALUES (345,17);";

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
            System.out.printf("%-10s%-12s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(3));
            System.out.println();
        }
    }

    public void useCard(String cardName) throws SQLException {
        int uID = 123;

        ResultSet rs = st.executeQuery("SELECT * FROM AppliedCard WHERE uID = " + uID + " and cardID = \n" +
                "(SELECT cardID FROM cardInfo WHERE cardName LIKE '%' || '" + cardName + "' || '%')");
        if (rs.next()) {
            if (Integer.parseInt(rs.getString(3)) <= 0) {
                //유저가 사용가능한 혜택을 모두 다 사용하였을 경우 에러출력
                System.out.println(cardName + "(카드)의 이번 달 혜택을 모두 사용하셨습니다.");
            } else {
                //사용가능한 혜택이 남아있을 경우 사용가능횟수를 1만큼 감소
                query = "UPDATE AppliedCard\n" +
                        "SET useRemain = useRemain-1\n" +
                        "WHERE uID = " + uID + " and cardID = \n" +
                        "(SELECT cardID FROM cardInfo WHERE cardName LIKE '%' || '" + cardName + "' || '%');";

                st.executeUpdate(query);
                System.out.println(cardName + "(카드)를 1회 사용했습니다.");
            }
        }


    }

    public void queryCard(String cardName) throws SQLException {
        int uID = 123;

        query = "SELECT useRemain \n" +
                "FROM AppliedCard \n" +
                "WHERE uID = " + uID + " AND cardID = \n" +
                "(SELECT cardID FROM cardInfo WHERE cardName LIKE '%' || '" + cardName + "' || '%');";

        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            int useRemainCount = Integer.parseInt(rs.getString(1));
            if (useRemainCount > 60) {
                System.out.println(cardName + "(카드)는 사용횟수의 제한이 없습니다.");
            } else if (useRemainCount > 0) {
                System.out.println(cardName + "(카드)의 사용가능횟수가 " + useRemainCount + "번 남았습니다");
            } else if (useRemainCount <= 0) {
                System.out.println(cardName + "(카드)의 이번 달 혜택을 모두 사용하셨습니다.");
            }
        }



    }

    public void showRemain() throws SQLException {
        int uID = 123;

        query = "SELECT cardName, useRemain\n" +
                "FROM appliedCard NATURAL JOIN cardInfo\n" +
                "WHERE uID = " + uID + ";";

        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-30s%-30s", "카드이름", "이번 달 남은 사용가능 횟수");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            String remainNotice = "";
            int useRemainCount = Integer.parseInt(rs.getString(2));
            if(useRemainCount>60){
                remainNotice = "사용횟수 제한 없음";
            }
            else if(useRemainCount>0){
                remainNotice = "남은 횟수 : "+useRemainCount+"회";
            }
            else if(useRemainCount<=0){
                remainNotice = "이번 달 혜택 모두 사용";
            }
            System.out.printf("%-30s%-30s", rs.getString(1), remainNotice);
            System.out.println();
        }



    }

    public void updateCardUse() throws SQLException {
        //trigger -> 카드사에서 이벤트로 혜택의 사용가능횟수를 늘렸을 경우 유저의 카드에도 바로 적용
        System.out.print("--[카드회사]-- 변경할 카드의 ID를 입력하세요. : ");
        String cardID = scan.nextLine();
        System.out.print("--[카드회사]-- 변경할 카드의 혜택횟수를 입력하세요. : ");
        String useCount_by_CP = scan.nextLine();

        query = "CREATE OR REPLACE FUNCTION updateAppliedCardInfo() RETURNS TRIGGER AS \n" +
                "$$\n" +
                "BEGIN\n" +
                "   update AppliedCard\n" +
                "   SET useRemain = New.monthlyUseCount\n" +
                "   WHERE cardID = New.cardID;\n" +
                "   RETURN NULL;\n" +
                "END\n" +
                "$$ LANGUAGE 'plpgsql';\n" +
                "CREATE TRIGGER updateAppliedCardInfoTrigger\n" +
                "AFTER UPDATE on cardInfo\n" +
                "FOR EACH ROW\n" +
                "EXECUTE PROCEDURE updateAppliedCardInfo();\n" +
                "UPDATE cardInfo SET monthlyUseCount = "+useCount_by_CP+" WHERE cardID = " + cardID + ";";

        st.executeUpdate(query);
        System.out.println("--[카드회사]-- 변경이 완료되었습니다.");

    }
}
