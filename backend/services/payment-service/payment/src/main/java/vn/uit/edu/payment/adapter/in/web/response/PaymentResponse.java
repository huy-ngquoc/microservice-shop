package vn.uit.edu.payment.adapter.in.web.response;

import java.time.Instant;

public record PaymentResponse(
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
