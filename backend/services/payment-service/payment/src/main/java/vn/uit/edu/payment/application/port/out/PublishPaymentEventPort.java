package vn.uit.edu.payment.application.port.out;


import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentCreatedFailDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentLinkCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

public interface PublishPaymentEventPort {
    public void publish(PaymentCreated event);
    public void publish(PaymentUpdated event);
    public void publishPaymentSuccess();
    public void publishPaymentFailed();
    
    public void publishPaymentExpired(OnlinePaymentExpiredDocument outboxEvent);

    
    public void publishPaymentSuccess(PaymentSuccessDocument outboxEvent);
    public void publishPaymentLinkCreated(PaymentLinkCreatedDocument outboxEvent);
    public void publishOnlinePaymentSuccess(OnlinePaymentSuccessDocument outboxEvent);
    public void publishPaymentCreatedFail(PaymentCreatedFailDocument outboxEvent);
}
