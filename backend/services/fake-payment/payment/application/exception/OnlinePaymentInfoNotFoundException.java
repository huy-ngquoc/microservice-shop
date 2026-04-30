package vn.uit.edu.payment.application.exception;

import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

public class OnlinePaymentInfoNotFoundException extends RuntimeException {
    public OnlinePaymentInfoNotFoundException(PaymentId paymentId) {
        super("Payment not found with id "+paymentId.value().toString());
    }
    public OnlinePaymentInfoNotFoundException(OnlinePaymentNumber onlinePaymentNumber) {
        super("Payment not found with payment number "+onlinePaymentNumber.value());
    }
    
}
