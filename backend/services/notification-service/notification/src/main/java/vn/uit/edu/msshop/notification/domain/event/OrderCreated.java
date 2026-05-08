package vn.uit.edu.msshop.notification.domain.event;

import java.util.UUID;

public record OrderCreated(
    UUID eventId,
    String currency,
    UUID orderId,
    String paymentMethod,
    long paymentValue,
    UUID userId,
    String userEmail
) {

}
