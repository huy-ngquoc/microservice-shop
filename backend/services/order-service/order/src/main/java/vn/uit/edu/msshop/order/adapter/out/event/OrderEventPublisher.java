package vn.uit.edu.msshop.order.adapter.out.event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher implements PublishOrderEventPort{
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String,OrderCreated> orderCreatedTemplate;
    private final KafkaTemplate<String,CodPaymentCancelled> codPaymentCancelledTemplate;
    private final KafkaTemplate<String,CodPaymentReceived> codPaymentReceivedTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-topic";
    private static final String PAYMENT_STATUS_TOPIC="payment-cod-topic";
   

    @Override
    public void publish(OrderUpdated orderUpdated) {
        eventPublisher.publishEvent(orderUpdated);
    }

    @Override
    public void publishOrderCreatedEvent(OrderCreated event) {
        Message<OrderCreated> message=MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, ORDER_CREATED_TOPIC).build();
        orderCreatedTemplate.send(message);
    }

    @Override
    public void publishCodPaymentCancelled(CodPaymentCancelled event) {
        Message<CodPaymentCancelled> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        codPaymentCancelledTemplate.send(message);
    }

    @Override
    public void publishCodPaymentReceived(CodPaymentReceived event) {
        Message<CodPaymentReceived> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        codPaymentReceivedTemplate.send(message);
    }

}
