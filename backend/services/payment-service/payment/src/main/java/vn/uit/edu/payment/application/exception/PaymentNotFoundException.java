package vn.uit.edu.payment.application.exception;

import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(PaymentId paymentId) {
        super("Payment not found with id "+paymentId.value().toString());
    }
}
