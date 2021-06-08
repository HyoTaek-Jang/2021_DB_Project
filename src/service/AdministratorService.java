package service;

import repository.AppliedCardRepository;
import repository.CafeRepository;
import repository.CardInfoRepository;
import repository.DiscountInfoRepository;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class AdministratorService {

    CafeRepository cafeRepository;
    CardInfoRepository cardInfoRepository;
    DiscountInfoRepository discountInfoRepository;
    AppliedCardRepository appliedCardRepository;

    private String query;

    public AdministratorService(Statement st) {
        cafeRepository = new CafeRepository(st);
        cardInfoRepository = new CardInfoRepository(st);
        discountInfoRepository = new DiscountInfoRepository(st);
        appliedCardRepository = new AppliedCardRepository(st);

    }

    public void setTables() throws SQLException {
        cafeRepository.createTable();
        System.out.println("cafe table 생성을 완료했습니다.");
        cafeRepository.importCSV();
        System.out.println("cafe CSV파일 import를 완료했습니다.");

        System.out.println();

        cardInfoRepository.createTable();
        System.out.println("cardInfo table 생성을 완료했습니다.");
        cardInfoRepository.importCSV();
        System.out.println("cardInfo CSV파일 import를 완료했습니다.");

        System.out.println();

        discountInfoRepository.createTable();
        System.out.println("discountInfo table 생성을 완료했습니다.");
        discountInfoRepository.importCSV();
        System.out.println("discountInfo CSV파일 import를 완료했습니다.");

        System.out.println();

        appliedCardRepository.createTable();
        System.out.println("appliedCard table 생성을 완료했습니다.");
        appliedCardRepository.insertAppliedCard();
        System.out.println("appliedCard table 데이터 import를 완료했습니다.");

    }

    public void showTables() throws SQLException {
        System.out.println("Start Cafe Table");
        cafeRepository.showTable();
        System.out.println("End Cafe Table");

        System.out.println("\nStart cardInfo Table");
        cardInfoRepository.showTable();
        System.out.println("End cardInfo Table");

        System.out.println("\nStart discountInfo Table");
        discountInfoRepository.showTable();
        System.out.println("End discountInfo Table");

        System.out.println("\nStart AppliedCard Table");
        appliedCardRepository.showTable();
        System.out.println("End AppliedCard Table");

    }

    public void updateAppliedCard(Statement st) throws SQLException {

        Scanner scan = new Scanner(System.in);
        //매달 1일 혜택 사용가능 횟수 초기화
        String date;
        System.out.print("오늘 날짜 입력 : ");
        date = scan.nextLine();
        if(Integer.parseInt(date)%100 == 1){
            System.out.println("카드혜택 사용횟수가 초기화됩니다.");
            query = "UPDATE AppliedCard \n" +
                    "SET useRemain = (SELECT monthlyUseCount FROM CardInfo WHERE AppliedCard.cardID = CardInfo.cardID);\n";

            st.executeUpdate(query);
            appliedCardRepository.showTable();
        }

        //유저가 카드를 사용한 경우 남아있는 사용횟수가 1 감소
        String uID, cID;
        System.out.print("사용자 id 입력 : ");
        uID = scan.nextLine();
        System.out.print("카드 id 입력 : ");
        cID = scan.nextLine();

        ResultSet rs = st.executeQuery("SELECT * FROM AppliedCard WHERE uID = "+uID+" and cardID = "+cID+";");
        if(rs.next()){
            if(Integer.parseInt(rs.getString(3)) <= 0){
                //유저가 사용가능한 혜택을 모두 다 사용하였을 경우 에러출력
                System.out.println("카드의 이번 달 혜택을 모두 사용하셨습니다.");
            }
            else{
                //사용가능한 혜택이 남아있을 경우 사용가능횟수를 1만큼 감소
                query = "UPDATE AppliedCard \n" +
                        "SET useRemain = useRemain-1 \n" +
                        "WHERE uID = "+uID+" and cardID = "+cID+";";
                st.executeUpdate(query);
                appliedCardRepository.showTable();
            }
        }



    }

}
