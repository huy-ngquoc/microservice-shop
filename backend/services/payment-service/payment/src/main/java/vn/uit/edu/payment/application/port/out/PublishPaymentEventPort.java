package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

public interface PublishPaymentEventPort {
    public void publish(PaymentCreated event);
    public void publish(PaymentUpdated event);
}
