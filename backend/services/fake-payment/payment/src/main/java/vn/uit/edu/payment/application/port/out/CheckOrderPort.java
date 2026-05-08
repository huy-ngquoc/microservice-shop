package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;

public interface CheckOrderPort {
    public void checkOrder(PaymentId paymentId, PaymentValue paymentValue, OrderId orderId);
}
