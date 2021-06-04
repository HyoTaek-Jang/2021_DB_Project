import config.SetUpDB;
import service.AdministratorService;

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
            st = SetUpDB.setUpDB();
        } catch (SQLException sqlEx) {
            System.err.println("SQLException");
        } finally {
            System.out.println("------------------------------------------------------------------");
        }

//        run program
//        init service
        AdministratorService adminService = new AdministratorService(st);

        System.out.println("Run Program");
        while (command != 0){
            System.out.println("0 : Exit Program");
            System.out.println("9 : Set Tables | Administrator only");
            System.out.println("10 : Show Tables | Administrator only");
            System.out.printf("원하시는 메뉴를 선택해주세요. : ");

            command = Integer.parseInt(scan.nextLine());

            System.out.println("------------------------------------------------------------------\n");

            switch (command) {
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    break;
                case 9:
                    adminService.setTables();
                    break;
                case 10:
                    adminService.showTables();
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");

            }

            System.out.println("\n------------------------------------------------------------------");


        }

    }

}
