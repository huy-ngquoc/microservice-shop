package vn.uit.edu.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentLink;
import vn.uit.edu.payment.domain.model.valueobject.TransactionId;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePaymentInfo {
    private PaymentId paymentId;
    private PaymentLink link;
    private OnlinePaymentNumber paymentNumber;
    private TransactionId transactionId;

}
