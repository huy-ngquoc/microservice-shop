package vn.uit.edu.payment.application.dto.command;

import java.util.Currency;

import org.jspecify.annotations.NullMarked;

import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@NullMarked
public record UpdatePaymentCommand(
    PaymentId paymentId,
    Currency currency,
    PaymentStatus paymentStatus,
    PaymentMethod paymentMethod
) {

}
