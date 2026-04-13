package vn.uit.edu.payment.application.dto.query;

import java.time.Instant;

public record PaymentView(
    String paymentId,
    Instant createAt,
    String currency,
    String orderId,
    String paymentMethod,
    String paymentStatus,
    long paymentValue,
    Instant updateAt,
    String userId
) {

}
