package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.event.CodPaymentCreated;
import vn.uit.edu.payment.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

public interface PublishPaymentEventPort {
    public void publish(PaymentCreated event);
    public void publish(PaymentUpdated event);
    public void publishPaymentSuccess();
    public void publishPaymentFailed();
    public void publishPaymentCancelled(OnlinePaymentCancelled event);
    public void publishPaymentExpired(OnlinePaymentExpired event);

    public void publishPaymentCodCreated(CodPaymentCreated event);
}
