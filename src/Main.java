import config.DB;
import service.AdministratorService;
import service.BenefitService;
import service.MapApi;
import service.CardUseService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("2021 DataBase Term Project - Team 두부");

//        variable init
        Statement st = null;
        int command = 1;
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("------------------------------------------------------------------");
            System.out.println("Connecting PostgreSQL database");
            st = DB.setUpDB();
        } catch (SQLException sqlEx) {
            try {
                DB.closeDB();
            } catch (SQLException sqlExc) {
                System.out.println("Error : "+ sqlExc);
            }
            System.out.println("Error : "+ sqlEx);
        } finally {
            System.out.println("------------------------------------------------------------------");
        }

//        run program
//        init service
        AdministratorService adminService = new AdministratorService(st);
        BenefitService benefitService = new BenefitService(st);
        MapApi mapApi  = new MapApi(st);
        CardUseService cardUseService = new CardUseService(st);

        System.out.println("Run Program");
        while (command != 0){
            System.out.println("0 : 프로그램 종료");
            System.out.println("1 : 카드 사용 및 남은 혜택 조회");
            System.out.println("2 : 가까운 가맹점 찾기");
            System.out.println("3 : 현재 위치 설정");
            System.out.println("4 : 혜택 조회");
            System.out.println("8 : 카트 혜택 가능횟수 변경 | 관리자 전용");
            System.out.println("9 : Set Tables | 관리자 전용");
            System.out.println("10 : Show Tables | 관리자 전용");
            System.out.print("원하시는 메뉴를 선택해주세요. : ");

            command = Integer.parseInt(scan.nextLine());

            System.out.println("------------------------------------------------------------------\n");
            try{
                switch (command) {
                    case 0 -> {
                        DB.closeDB();
                        System.out.println("프로그램을 종료합니다.");
                    }
                    case 1 -> cardUseService.searchCardUse();
                    case 2 -> mapApi.nearCafe(123);
                    case 3 -> mapApi.setUserLocation(123);
                    case 4 -> benefitService.searchBenefit();
                    case 8 -> cardUseService.updateCard();
                    case 9 -> adminService.setTables();
                    case 10 -> adminService.showTables();
                    default -> System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }catch (SQLException sqlEx) {
                System.out.println("Error : "+ sqlEx);
            } catch (Exception e) {
                System.out.println("Error : "+ e);
            }

            System.out.println("\n------------------------------------------------------------------");


        }

    }

}
