package ch03;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {

        int addedMonths = payData.getPaymentAmount() / 100_000 * 12 + payData.getPaymentAmount() % 100_000 / 10_000;
        if(payData.getFirstBillingDate() != null ) {
            return expiryDateUsingFirstBillingDate(payData, addedMonths);
        } else {
            return payData.getBillingDate().plusMonths(addedMonths);
        }
    }

    @NotNull
    private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addedMonths) {
        LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
        if(isSameDayOfMonth(payData.getBillingDate(), payData.getFirstBillingDate())) {
            final int dayLenOfCandiMonth = lastDayOfMonth(candidateExp);
            final int dayOfFirstBillingDate = payData.getFirstBillingDate().getDayOfMonth();
            if (dayLenOfCandiMonth < dayOfFirstBillingDate) {
                return candidateExp.withDayOfMonth(dayLenOfCandiMonth);
            }
            return candidateExp.withDayOfMonth(dayOfFirstBillingDate);
        } else {
            return candidateExp;
        }
    }

    private int lastDayOfMonth(LocalDate candidateExp) {
        return YearMonth.from(candidateExp).lengthOfMonth();
    }

    private boolean isSameDayOfMonth(LocalDate billingDate, LocalDate firstBillingDate) {
        return firstBillingDate.getDayOfMonth() != billingDate.getDayOfMonth();
    }
}
