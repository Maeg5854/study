package ch03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * chapter 3 - 테스트 작성 순서 연습하기
 * 매달 비용을 지불해야 사용할 수 있는 유료 서비스가 있다고 해보자. 이 서비스는 다음 규칙에 따라 서비스 만료일을 결정한다.
 * - 서비스를 사용하려면 매달 1만 원을 선불로 납부한다. 납부일 기준으로 한 달 뒤가 서비스 만료일이 된다.
 * - 2개월 이상 요금을 납부할 수 있다.
 * - 10만 원을 납부하면 서비스를 1년 제공한다.
 *
 * 납부한 금액 기준으로 서비스 만료일을 계산하는 기능을 TDD로 구현한다면 어떤 순서로 진행해야 할까?
 *
 * 테스트 케이스 선정 기준
 * - 쉬운거 -> 어려운거
 * - 예외상황 -> 정상 상황
 *
 * 1) 제일 간단한 구현하기 : 1만원을 냈을 때 1달 뒤 반환
 * 2) 코드정리하기 : 중복제거 - 테스트코드에서 할만한거 있었음
 */
public class ExpiryDateCalculatorTest {
    ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();

    @Test
    public void 일_만원_납부시_한_달_뒤_만료 () {
        assertExpiryDate(LocalDate.of(2019, 4, 1),10000, LocalDate.of(2019, 5, 1));
        assertExpiryDate(LocalDate.of(2019, 4, 1), 10000, LocalDate.of(2019, 5, 1));
    }

    private void assertExpiryDate(LocalDate billingDate, int billingAmount, LocalDate expectedExpiryDate) {
        LocalDate expiryDate = expiryDateCalculator.calculate(billingDate, billingAmount);
        assertEquals(expectedExpiryDate, expiryDate);
    }
}
