package vn.uit.edu.payment.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.domain.event.CodPaymentCreated;
import vn.uit.edu.payment.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher implements PublishPaymentEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String,OnlinePaymentCancelled> paymentCancelledTemplate;
    private final KafkaTemplate<String,OnlinePaymentExpired> paymentExpiredTemplate;
    private final KafkaTemplate<String, CodPaymentCreated> codPaymentCreatedTemplate;
    private static final String PAYMENT_TOPIC="payment-online-topic";
    @Override
    public void publish(PaymentCreated event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publish(PaymentUpdated event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishPaymentSuccess() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void publishPaymentFailed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void publishPaymentCancelled(OnlinePaymentCancelled cancelled) {
        Message<OnlinePaymentCancelled> message = MessageBuilder.withPayload(cancelled).setHeader(KafkaHeaders.TOPIC, PAYMENT_TOPIC).build();
        paymentCancelledTemplate.send(message);
        
    }

    @Override
    public void publishPaymentExpired(OnlinePaymentExpired event) {
        Message<OnlinePaymentExpired> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_TOPIC).build();
        paymentExpiredTemplate.send(message);
    }

    @Override
    public void publishPaymentCodCreated(CodPaymentCreated event) {
        Message<CodPaymentCreated> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_TOPIC).build();
        codPaymentCreatedTemplate.send(message);
    }

}
