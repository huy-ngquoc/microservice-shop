package vn.uit.edu.payment.domain.event;

import java.util.UUID;

public record OnlinePaymentFail(
        UUID orderId) {

}
