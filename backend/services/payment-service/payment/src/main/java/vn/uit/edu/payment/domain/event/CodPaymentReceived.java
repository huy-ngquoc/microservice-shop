package vn.uit.edu.payment.domain.event;

import java.util.UUID;

public record CodPaymentReceived(UUID orderId, UUID eventId) {

}
