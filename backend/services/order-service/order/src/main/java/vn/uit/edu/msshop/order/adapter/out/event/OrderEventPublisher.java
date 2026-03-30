package vn.uit.edu.msshop.order.adapter.out.event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderCreatedSuccess;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCancelled;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderShipped;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher implements PublishOrderEventPort{
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String,OrderCreated> orderCreatedTemplate;
    private final KafkaTemplate<String,CodPaymentCancelled> codPaymentCancelledTemplate;
    private final KafkaTemplate<String,CodPaymentReceived> codPaymentReceivedTemplate;
    private final KafkaTemplate<String, OrderCreatedSuccess> clearCartTemplate;
    private final KafkaTemplate<String, vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated> inventoryOrderCreatedTemplate; 
    private final KafkaTemplate<String, OrderCancelled> orderCancelledTemplate;
    private final KafkaTemplate<String, OrderShipped> orderShippedTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-topic";
    private static final String PAYMENT_STATUS_TOPIC="payment-cod-topic";
    private static final String CLEAR_CART_TOPIC="cart-topic";
    private static final String INVENTORY_TOPIC="order-inventory";
    private final OrderCreatedOutboxPublisher orderCreatedOutboxPublisher;
    private final CodPaymentCancelledOutboxPublisher codPaymentCancelledOutboxPublisher;
   
    /*UUID eventId,
    String currency,
    UUID orderId,
    String paymentMethod,
    long paymentValue */
    @Override
    public void publish(OrderUpdated orderUpdated) {
        eventPublisher.publishEvent(orderUpdated);
    }

    @Override
    public void publishOrderCreatedEvent( OrderCreatedDocument outboxEvent) {
        OrderCreated event = new OrderCreated(outboxEvent.getEventId(),outboxEvent.getCurrency(), outboxEvent.getOrderId(), outboxEvent.getPaymentMethod(), outboxEvent.getPaymentValue());
        Message<OrderCreated> message=MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, ORDER_CREATED_TOPIC).build();
        orderCreatedTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                orderCreatedOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Fail, wait 5 seconds");
            }
        });
    }

    @Override
    public void publishCodPaymentCancelled(CodPaymentCancelledDocument outboxEvent) {
        CodPaymentCancelled event = new CodPaymentCancelled(outboxEvent.getOrderId(), outboxEvent.getEventId());
        Message<CodPaymentCancelled> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        codPaymentCancelledTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                codPaymentCancelledOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Fail, wait 5 seconds");
            }
        });
    }

    @Override
    public void publishCodPaymentReceived(CodPaymentReceived event) {
        Message<CodPaymentReceived> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        codPaymentReceivedTemplate.send(message);
    }

    @Override
    public void publishClearCartEvent(OrderCreatedSuccess event) {
        Message<OrderCreatedSuccess> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, CLEAR_CART_TOPIC).build();
        clearCartTemplate.send(message);

    }

    @Override
    public void publishOrderCreated_InventoryEvent(vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated event) {
        Message<vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated > message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();
        inventoryOrderCreatedTemplate.send(message);
    }

    @Override
    public void publishOrderCancelled_InventoryEvent(OrderCancelled event) {
        Message<OrderCancelled> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();
        orderCancelledTemplate.send(message);
    }

    @Override
    public void publishOrderShipped_InventoryEvent(OrderShipped event) {
         Message<OrderShipped> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();
        orderShippedTemplate.send(message);
    }

}
