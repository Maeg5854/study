package ch03;

import java.time.LocalDate;

public class ExpiryDateCalculator {
    public LocalDate calculate(LocalDate billingDate, int moneyWon) {
        if (billingDate.equals(LocalDate.of(2019,4,1))) return LocalDate.of(2019,5,1);
        return billingDate.plusMonths(1);
    }
}
