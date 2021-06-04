package service;

import repository.CafeRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class AdministratorService {
    CafeRepository cafeRepository;

    public AdministratorService(Statement st) {
        cafeRepository = new CafeRepository(st);

    }

    public void setTables() throws SQLException {
        cafeRepository.createCafeTable();
        System.out.println("cafe table 생성을 완료했습니다.");

        cafeRepository.importCSV();
        System.out.println("CSV파일 import를 완료했습니다.");

    }

    public void showTables() throws SQLException {
        System.out.println("Start Cafe Tables");
        cafeRepository.showTable();
        System.out.println("End Cafe Tables");


    }
}
