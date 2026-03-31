package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.adapter.out.event.CodPaymentCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.OnlinePaymentCancelledDocument;
import vn.uit.edu.payment.adapter.out.event.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

public interface PublishPaymentEventPort {
    public void publish(PaymentCreated event);
    public void publish(PaymentUpdated event);
    public void publishPaymentSuccess();
    public void publishPaymentFailed();
    public void publishPaymentCancelled(OnlinePaymentCancelledDocument outboxEvent);
    public void publishPaymentExpired(OnlinePaymentExpiredDocument outboxEvent);

    public void publishPaymentCodCreated(CodPaymentCreatedDocument outboxEvent);
}
