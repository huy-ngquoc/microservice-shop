package vn.uit.edu.payment.adapter.in.web.request;

import java.util.UUID;

public record CreatePaymentRequest(
    String currency,
    UUID orderId,
    String paymentMethod,
    long paymentValue
) {

}
