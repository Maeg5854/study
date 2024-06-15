package ch03;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class PayData {
    private LocalDate billingDate;
    private int paymentAmount;
    private LocalDate firstBillingDate;
}
