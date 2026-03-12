package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

public record OnlinePaymentExpired(UUID orderId) {

}
