package vn.uit.edu.payment.application.port.in;

import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

public interface LoadPaymentUseCase {
    public PaymentView findById(PaymentId paymentId);
    public PaymentView loadByOrderId(OrderId orderId);
}
