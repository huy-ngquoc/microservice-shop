package vn.uit.edu.payment.domain.event;

import java.util.UUID;

public record CodPaymentCancelled(
        UUID orderId,
        UUID eventId) {

}
