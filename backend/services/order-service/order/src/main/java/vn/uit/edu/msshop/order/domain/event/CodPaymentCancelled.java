package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

public record CodPaymentCancelled(UUID orderId, UUID eventId) {

}
