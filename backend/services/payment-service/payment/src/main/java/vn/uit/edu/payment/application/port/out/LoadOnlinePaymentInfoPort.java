package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;
import vn.uit.edu.payment.domain.model.valueobject.OnlinePaymentNumber;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

public interface LoadOnlinePaymentInfoPort {
    public OnlinePaymentInfo loadById(PaymentId id);
    public OnlinePaymentInfo loadByOrderCode(OnlinePaymentNumber paymentNumber);

}
