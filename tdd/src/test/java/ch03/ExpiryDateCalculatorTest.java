package ch03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        PayData payData = PayData.builder()
                .billingDate(LocalDate.of(2019, 4, 1))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData, LocalDate.of(2019, 5, 1));
    }

    @Test
    public void 납부일과_한달_뒤_일자가_같지_않을_경우 () {
        PayData payData1 = PayData.builder()
            .billingDate(LocalDate.of(2019, 1, 31))
            .paymentAmount(10000)
            .build();
        assertExpiryDate(payData1, LocalDate.of(2019, 2, 28));

        PayData payData2 = PayData.builder()
                .billingDate(LocalDate.of(2019, 5, 31))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData2, LocalDate.of(2019, 6, 30));

        PayData payData3 = PayData.builder()
                .billingDate(LocalDate.of(2020, 1, 31))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData3, LocalDate.of(2020, 2, 29));
    }

    @Test
    public void 첫_납부일과_만료일의_일자가_다를때_1만원_납부 () {
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,1,31))
                .billingDate(LocalDate.of(2019,2,28))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData, LocalDate.of(2019, 3, 31));

        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,1,30))
                .billingDate(LocalDate.of(2019,2,28))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData2, LocalDate.of(2019, 3, 30));

        PayData payData3 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,3,31))
                .billingDate(LocalDate.of(2019,4,30))
                .paymentAmount(10000)
                .build();
        assertExpiryDate(payData3, LocalDate.of(2019, 5, 31));
    }

    @Test
    public void 이만원_이상_납부하면_비례해서_만료일_계산() {
        assertExpiryDate(
            PayData.builder()
                .billingDate(LocalDate.of(2019,4,15))
                .paymentAmount(20000)
                .build()
            ,LocalDate.of(2019,6,15));

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,4,15))
                        .paymentAmount(30000)
                        .build()
                ,LocalDate.of(2019,7,15));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,4,15))
                        .paymentAmount(70000)
                        .build()
                ,LocalDate.of(2019,11,15));
    }

    @Test
    public void 십만원_납부시_서비스로_1년제공 () {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,1,10))
                        .paymentAmount(100000)
                        .build()
                ,LocalDate.of(2020,1,10));

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2020,2,29))
                        .paymentAmount(100000)
                        .build()
                ,LocalDate.of(2021,2,28));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2020,1,10))
                        .paymentAmount(130000)
                        .build()
                ,LocalDate.of(2021,4,10));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2020,2,29))
                        .paymentAmount(130000)
                        .build()
                ,LocalDate.of(2021,5,29));
    }

    @Test
    public void 이만원_이상_납부할때_첫납부일과_납부일의_일자가_다를때() {
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 1, 31))
                        .billingDate(LocalDate.of(2019,2,28))
                        .paymentAmount(20000)
                        .build()
                ,LocalDate.of(2019,4,30));
    }

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        LocalDate expiryDate = expiryDateCalculator.calculate(payData);
        assertEquals(expectedExpiryDate, expiryDate);
    }
}
