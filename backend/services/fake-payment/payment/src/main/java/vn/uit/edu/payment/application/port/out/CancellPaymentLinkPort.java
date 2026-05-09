package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.valueobject.OrderId;

public interface CancellPaymentLinkPort {
    public void cancelPaymentLink(
            OrderId orderId);
}
