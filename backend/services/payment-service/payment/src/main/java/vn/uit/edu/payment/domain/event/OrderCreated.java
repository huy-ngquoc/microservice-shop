package vn.uit.edu.payment.domain.event;

import java.util.UUID;

public record OrderCreated(
    String currency,
    UUID orderId,
    String paymentMethod,
    long paymentValue) {

}
