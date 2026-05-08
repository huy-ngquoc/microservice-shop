package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.OnlinePaymentInfo;

public interface SaveOnlinePaymentInfoPort {
    public OnlinePaymentInfo save(
            OnlinePaymentInfo onlinePaymentInfo);
}
