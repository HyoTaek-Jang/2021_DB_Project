package service;

import repository.CafeRepository;
import repository.CardInfoRepository;
import repository.DiscountInfoRepository;
import repository.AppliedCardRepository;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class CardUseService {

    CafeRepository cafeRepository;
    CardInfoRepository cardInfoRepository;
    DiscountInfoRepository discountInfoRepository;
    AppliedCardRepository appliedCardRepository;


    public CardUseService(Statement st) {
        cafeRepository = new CafeRepository(st);
        cardInfoRepository = new CardInfoRepository(st);
        discountInfoRepository = new DiscountInfoRepository(st);
        appliedCardRepository = new AppliedCardRepository(st);
    }


    Scanner scan = new Scanner(System.in);

    public void searchCardUse() throws SQLException {
        System.out.println("검색 옵션 선택");
        System.out.println("1. 카드 사용하기");
        System.out.println("2. 카드 사용가능횟수 조회");
        System.out.println("3. 카드 사용가능횟수 조회(전체)");
        System.out.print("원하시는 검색 옵션를 입력하세요. : ");
        switchOption(parseInt(scan.nextLine()));
    }

    public void switchOption(int option) throws SQLException {
        switch (option) {
            case 1 -> useCard();
            case 2 -> showUseRemain();
            case 3 -> showUseRemain_all();
            default -> System.out.println("잘못된 입력입니다. 메뉴 선택으로 돌아갑니다.");

        }
    }

    public void useCard() throws SQLException {
        System.out.print("사용할 카드의 이름을 입력하세요. : ");
        String cardName = scan.nextLine();
        appliedCardRepository.useCard(cardName);
    }


    public void showUseRemain() throws SQLException {
        System.out.print("조회할 카드의 이름을 입력하세요. : ");
        String cardName = scan.nextLine();
        appliedCardRepository.queryCard(cardName);
    }
    public void showUseRemain_all() throws SQLException {
        System.out.println();
        appliedCardRepository.showRemain();
    }

    public void updateCard() throws SQLException {
        System.out.println("카드 사용가능횟수를 변경합니다");
        appliedCardRepository.updateCardUse();
    }
}
