package vn.uit.edu.payment.application.port.out;

import java.util.Optional;

import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

public interface LoadPaymentPort {
    public Optional<Payment> loadPaymentById(PaymentId paymentId);
    public Payment loadPaymentByOrderId(OrderId orderId);
}
