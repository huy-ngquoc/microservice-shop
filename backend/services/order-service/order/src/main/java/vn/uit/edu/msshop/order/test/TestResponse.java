package vn.uit.edu.msshop.order.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {
    private long confirmedPaid;
    private long confirmedUnpaid;
    private long error;
    private long paymentExpired;

}
