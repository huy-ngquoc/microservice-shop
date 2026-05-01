package vn.uit.edu.payment.application.dto.command;


import org.jspecify.annotations.NullMarked;

import vn.uit.edu.payment.application.common.Change;
import vn.uit.edu.payment.domain.model.valueobject.Currency;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentMethod;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@NullMarked
public record UpdatePaymentCommand(
    PaymentId paymentId,
    Change<Currency> currency,
    Change<PaymentStatus> paymentStatus,
    Change<PaymentMethod> paymentMethod
) {

}
