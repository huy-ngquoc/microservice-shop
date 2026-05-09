package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.domain.model.valueobject.OrderId;

public interface LoadOnlinePaymentUseCase {
    public String getPaymentLink(
            OrderId orderId);
}
