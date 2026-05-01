package vn.uit.edu.payment.application.dto.command;

import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentLink;
import vn.uit.edu.payment.domain.model.valueobject.TransactionId;

public record CreateOnlinePaymentInfoCommand( PaymentId paymentId,
    PaymentLink link,
    OnlinePaymentNumber paymentNumber,
     TransactionId transactionId) {

}
