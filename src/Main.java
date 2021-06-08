import config.DB;
import service.AdministratorService;
import service.BenefitService;
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
        CardUseService cardUseService = new CardUseService(st);

        System.out.println("Run Program");
        while (command != 0){
            System.out.println("0 : Exit Program");
            System.out.println("1 : Use Card and Show Remains");
            System.out.println("8 : Search Benefits");
            System.out.println("9 : Set Tables | Administrator only");
            System.out.println("10 : Show Tables | Administrator only");
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
                    case 8 -> benefitService.searchBenefit();
                    case 9 -> adminService.setTables();
                    case 10 -> adminService.showTables();
                    default -> System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }catch (SQLException sqlEx) {
                System.out.println("Error : "+ sqlEx);
            }

            System.out.println("\n------------------------------------------------------------------");


        }

    }

}
