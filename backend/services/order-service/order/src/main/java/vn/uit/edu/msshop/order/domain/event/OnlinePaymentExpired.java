package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

public record OnlinePaymentExpired(UUID eventId,UUID orderId, String userEmail) {

}
