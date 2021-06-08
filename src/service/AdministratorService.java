package service;

import repository.CafeRepository;
import repository.CardInfoRepository;
import repository.DiscountInfoRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class AdministratorService {
    CafeRepository cafeRepository;
    CardInfoRepository cardInfoRepository;
    DiscountInfoRepository discountInfoRepository;

    public AdministratorService(Statement st) {
        cafeRepository = new CafeRepository(st);
        cardInfoRepository = new CardInfoRepository(st);
        discountInfoRepository = new DiscountInfoRepository(st);
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


    }
}
