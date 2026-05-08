package vn.uit.edu.payment.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentCreatedFailDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentLinkCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.publisher.OnlinePaymentExpiredOutboxPublisher;
import vn.uit.edu.payment.adapter.out.event.publisher.OnlinePaymentSuccessOutboxPublisher;
import vn.uit.edu.payment.adapter.out.event.publisher.PaymentCreatedFailOutboxPublisher;
import vn.uit.edu.payment.adapter.out.event.publisher.PaymentLinkCreatedOutboxPublisher;
import vn.uit.edu.payment.adapter.out.event.publisher.PaymentSuccessOutboxPublisher;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.domain.event.CodPaymentCreated;
import vn.uit.edu.payment.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;
import vn.uit.edu.payment.domain.event.OnlinePaymentSuccess;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentCreatedFail;
import vn.uit.edu.payment.domain.event.PaymentLinkCreated;
import vn.uit.edu.payment.domain.event.PaymentSuccess;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher implements PublishPaymentEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String, OnlinePaymentCancelled> paymentCancelledTemplate;
    private final KafkaTemplate<String, PaymentCreatedFail> paymentCreatedFailTemplate;
    private final KafkaTemplate<String, OnlinePaymentExpired> paymentExpiredTemplate;
    private final KafkaTemplate<String, CodPaymentCreated> codPaymentCreatedTemplate;
    private final KafkaTemplate<String, PaymentSuccess> paymentSuccessTemplate;
    private final KafkaTemplate<String, PaymentLinkCreated> paymentLinkCreatedTemplate;
    private final KafkaTemplate<String, OnlinePaymentSuccess> onlinePaymentSuccessTemplate;
    private final OnlinePaymentSuccessOutboxPublisher onlinePaymentSuccessOutboxPublisher;
    private final PaymentLinkCreatedOutboxPublisher paymentLinkCreatedOutboxPublisher;

    private final OnlinePaymentExpiredOutboxPublisher onlinePaymentExpiredOutboxPublisher;

    private final PaymentSuccessOutboxPublisher paymentSuccessOutboxPublisher;
    private final PaymentCreatedFailOutboxPublisher paymentCreatedFailOutboxPublisher;
    private static final String PAYMENT_ONLINE_TOPIC = "payment-online-topic";
    private static final String PAYMENT_TOPIC = "payment-topic";

    @Override
    public void publish(
            PaymentCreated event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publish(
            PaymentUpdated event) {
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
    public void publishPaymentExpired(
            OnlinePaymentExpiredDocument outboxEvent) {
        OnlinePaymentExpired event = new OnlinePaymentExpired(outboxEvent.getOrderId(), outboxEvent.getEventId(),
                outboxEvent.getUserEmail(), outboxEvent.getUserId());
        Message<OnlinePaymentExpired> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_ONLINE_TOPIC).build();
        try {
            paymentExpiredTemplate.send(message).whenComplete((
                    result,
                    ex) -> {
                if (ex == null) {
                    onlinePaymentExpiredOutboxPublisher.markAsSent(outboxEvent);
                } else {
                    System.out.println("Send fail");
                }
            });
        } catch (Exception e) {
            log.error("Error sending event");
        }
    }

    @Override
    public void publishPaymentSuccess(
            PaymentSuccessDocument outboxEvent) {
        PaymentSuccess event = new PaymentSuccess(outboxEvent.getEventId(), outboxEvent.getOrderId());
        Message<PaymentSuccess> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_ONLINE_TOPIC).build();
        try {
            paymentSuccessTemplate.send(message).whenComplete((
                    result,
                    ex) -> {
                if (ex == null) {
                    paymentSuccessOutboxPublisher.markAsSent(outboxEvent);
                } else {
                    System.out.println("Send fail");
                }
            });
        } catch (Exception e) {
            log.error("Error sending event");
        }
    }

    @Override
    public void publishPaymentLinkCreated(
            PaymentLinkCreatedDocument outboxEvent) {
        PaymentLinkCreated event = new PaymentLinkCreated(outboxEvent.getEventId(), outboxEvent.getPaymentLink(),
                outboxEvent.getOrderId(), outboxEvent.getUserEmail(), outboxEvent.getUserId());
        Message<PaymentLinkCreated> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_ONLINE_TOPIC).build();
        try {
            paymentLinkCreatedTemplate.send(message).whenComplete((
                    result,
                    ex) -> {
                if (ex == null) {
                    paymentLinkCreatedOutboxPublisher.markAsSent(outboxEvent);
                } else {
                    System.out.println("Send fail");
                }
            });
        } catch (Exception e) {
            log.error("Error sending event");
        }
    }

    @Override
    public void publishOnlinePaymentSuccess(
            OnlinePaymentSuccessDocument outboxEvent) {
        OnlinePaymentSuccess event = new OnlinePaymentSuccess(outboxEvent.getEventId(), outboxEvent.getOrderId(),
                outboxEvent.getUserEmail(), outboxEvent.getUserId());
        System.out.println("Send online payment success event");
        Message<OnlinePaymentSuccess> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_ONLINE_TOPIC).build();
        try {
            onlinePaymentSuccessTemplate.send(message).whenComplete((
                    result,
                    ex) -> {
                if (ex == null) {
                    onlinePaymentSuccessOutboxPublisher.markAsSent(outboxEvent);
                } else {
                    System.out.println("Send fail");
                }
            });
        } catch (Exception e) {
            log.error("Error sending event");
        }
    }

    @Override
    public void publishPaymentCreatedFail(
            PaymentCreatedFailDocument outboxEvent) {
        PaymentCreatedFail event = new PaymentCreatedFail(outboxEvent.getEventId(), outboxEvent.getOrderId(),
                outboxEvent.getUserId(), outboxEvent.getUserEmail());
        Message<PaymentCreatedFail> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_TOPIC).build();
        try {
            paymentCreatedFailTemplate.send(message).whenComplete((
                    result,
                    ex) -> {
                if (ex == null) {
                    paymentCreatedFailOutboxPublisher.markAsSent(outboxEvent);
                } else {
                    System.out.println("Send fail");
                }
            });
        } catch (Exception e) {
            log.error("Error sending event");
        }
    }

}
