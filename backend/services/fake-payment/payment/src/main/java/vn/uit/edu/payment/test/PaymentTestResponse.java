package vn.uit.edu.payment.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTestResponse {
    private long pending;
    private long expired;
    private long success;
}
