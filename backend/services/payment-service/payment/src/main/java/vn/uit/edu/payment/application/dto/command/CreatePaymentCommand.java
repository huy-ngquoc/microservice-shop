package vn.uit.edu.payment.application.dto.command;



import vn.uit.edu.payment.domain.model.valueobject.Currency;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;
import vn.uit.edu.payment.domain.model.valueobject.PaymentValue;
import vn.uit.edu.payment.domain.model.valueobject.UserId;

public record CreatePaymentCommand(
    PaymentId paymentId,
    Currency currency,
    OrderId orderId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    PaymentValue paymentValue,
    UserId userId
) {
    public CreatePaymentCommand {
        if(paymentId==null) {
                 throw new IllegalArgumentException("Id must NOT be null");
        }
            if(currency==null) {
                throw new IllegalArgumentException("Currency must not be null");
            } 
            if(orderId==null) {
                throw new IllegalArgumentException("Order id must not be null");
            }
            if(paymentMethod==null) {
                throw new IllegalArgumentException("Payment method must not be null");
            }
            if(paymentStatus==null) {
                throw new IllegalArgumentException("Payment status must not be null");
            }
            if(paymentValue == null) {
                throw new IllegalArgumentException("Payment value must not be null");
            }
    }
}
