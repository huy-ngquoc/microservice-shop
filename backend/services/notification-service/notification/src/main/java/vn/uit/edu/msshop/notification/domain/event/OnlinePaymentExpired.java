package vn.uit.edu.msshop.notification.domain.event;

import java.util.UUID;

public record OnlinePaymentExpired(UUID orderId, UUID eventId, String userEmail) {

}
