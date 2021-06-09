package service;

import repository.CafeRepository;
import repository.CardInfoRepository;
import repository.DiscountInfoRepository;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class BenefitService {

    CafeRepository cafeRepository;
    CardInfoRepository cardInfoRepository;
    DiscountInfoRepository discountInfoRepository;

    public BenefitService(Statement st) {
        cafeRepository = new CafeRepository(st);
        cardInfoRepository = new CardInfoRepository(st);
        discountInfoRepository = new DiscountInfoRepository(st);
    }


    Scanner scan = new Scanner(System.in);

    public void searchBenefit() throws SQLException {
        System.out.println("검색 옵션 선택");
        System.out.println("1.점포명");
        System.out.println("2.카드명");
        System.out.print("원하시는 검색 옵션를 입력하세요. : ");
        switchOption(parseInt(scan.nextLine()));
    }

    public void switchOption(int option) throws SQLException {
        switch (option) {
            case 1 -> brandSearch();
            case 2 -> cardSearch();
            default -> System.out.println("잘못된 입력입니다. 메뉴 선택으로 돌아갑니다.");
        }
    }

    public void brandSearch() throws SQLException {
        System.out.print("검색을 원하시는 점포의 이름을 입력하세요. : ");
        String brandName = scan.nextLine();
        discountInfoRepository.queryBrand(brandName);
    }


    public void cardSearch() throws SQLException {
        System.out.print("검색을 카드의 이름을 입력하세요. : ");
        String cardName = scan.nextLine();
        discountInfoRepository.queryCardName(cardName);
    }
}
