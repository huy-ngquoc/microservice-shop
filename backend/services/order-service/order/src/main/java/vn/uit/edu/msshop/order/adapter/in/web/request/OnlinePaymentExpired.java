package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.UUID;

public record OnlinePaymentExpired(UUID orderId, UUID eventId, String userEmail, UUID userId) {

}
