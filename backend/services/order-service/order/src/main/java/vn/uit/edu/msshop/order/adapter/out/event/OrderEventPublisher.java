package vn.uit.edu.msshop.order.adapter.out.event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountEventsDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OnlinePaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;

import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCreatedInventoryDocument;

import vn.uit.edu.msshop.order.adapter.out.event.publisher.CodPaymentCancelledOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.CodPaymentReceivedOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.IncreaseSoldCountEventOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OnlinePaymentCancelledOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderCreatedOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderCreatedSuccessOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderUpdatedOutboxPublisher;

import vn.uit.edu.msshop.order.adapter.out.event.publisher.inventory.OrderCreatedInventoryOutboxPublisher;

import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;
import vn.uit.edu.msshop.order.domain.event.IncreaseSoldCountDetail;
import vn.uit.edu.msshop.order.domain.event.IncreaseSoldCountEvents;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderCreatedSuccess;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdatedEvent;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderCancelled;
import vn.uit.edu.msshop.order.domain.event.inventory.OrderDetail;
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
    private final KafkaTemplate<String, IncreaseSoldCountEvents> increaseSoldCountTemplate;
    private final KafkaTemplate<String,OnlinePaymentCancelled> onlinePaymentCancelledTemplate;
    private final KafkaTemplate<String, OrderUpdatedEventDocument> orderUpdatedEventDocumentTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-topic";
    private static final String PAYMENT_STATUS_TOPIC="payment-status-topic";
    private static final String CLEAR_CART_TOPIC="cart-topic";
    private static final String INVENTORY_TOPIC="order-inventory";
    private static final String PRODUCT_TOPIC="order-product";
    private final OrderCreatedOutboxPublisher orderCreatedOutboxPublisher;
    private final CodPaymentCancelledOutboxPublisher codPaymentCancelledOutboxPublisher;
    private final CodPaymentReceivedOutboxPublisher codPaymentReceivedOutboxPublisher;
    private final OrderCreatedSuccessOutboxPublisher orderCreatedSuccessOutboxPublisher;
    private final OrderCreatedInventoryOutboxPublisher orderCreatedInventoryOutboxPublisher;
   
    private final IncreaseSoldCountEventOutboxPublisher increaseSoldCountEventOutboxPublisher;
    private final OnlinePaymentCancelledOutboxPublisher onlinePaymentCancelledOutboxPublisher;
    private final OrderUpdatedOutboxPublisher orderUpdatedOutboxPublisher;
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
        
        OrderCreated event = new OrderCreated(outboxEvent.getEventId(),outboxEvent.getCurrency(), outboxEvent.getOrderId(), outboxEvent.getPaymentMethod(), outboxEvent.getPaymentValue(), outboxEvent.getUserId(), outboxEvent.getUserEmail());
        Message<OrderCreated> message=MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, ORDER_CREATED_TOPIC).build();
        try {
        orderCreatedTemplate.send(message)
        
        .whenComplete((result,ex)->{
            if(ex==null) {
                orderCreatedOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //log.error("Fail, wait 5 seconds");
            }
        });
    }
    catch(Exception e) {
        //System.out.println("Kafka is dead");
    e.printStackTrace();
    }
    }

    @Override
    public void publishCodPaymentCancelled(CodPaymentCancelledDocument outboxEvent) {
        CodPaymentCancelled event = new CodPaymentCancelled(outboxEvent.getOrderId(), outboxEvent.getEventId());
        Message<CodPaymentCancelled> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        try {
        codPaymentCancelledTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                codPaymentCancelledOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //log.error("Fail, wait 5 seconds");
            }
        });
    }
    catch(Exception e) {
        //log.error("Error sending event");
    }
    }

    @Override
    public void publishCodPaymentReceived(CodPaymentReceivedDocument outboxEvent) {
        CodPaymentReceived event = new CodPaymentReceived(outboxEvent.getOrderId(), outboxEvent.getEventId());
        Message<CodPaymentReceived> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        try {
        codPaymentReceivedTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                codPaymentReceivedOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
               // log.error("Fail, wait 5 seconds");
            }
        });
    }
    catch(Exception e) {
        //log.error("Error sending event");
    }
    }

    @Override
    public void publishClearCartEvent(OrderCreatedSuccessDocument outboxEvent) {
        OrderCreatedSuccess event = new OrderCreatedSuccess(outboxEvent.getEventId(), outboxEvent.getUserId(), outboxEvent.getVariantIds());
        Message<OrderCreatedSuccess> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, CLEAR_CART_TOPIC).build();
        try {
        clearCartTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                orderCreatedSuccessOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //log.error("Fail, wait 5 seconds");
            }
        });
    }catch(Exception e) {
        //System.out.println("Kafka is dead");
    e.printStackTrace();
    }

    }

    @Override
    public void publishOrderCreated_InventoryEvent(OrderCreatedInventoryDocument outboxEvent) {
        vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated event = new vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated(
            outboxEvent.getEventId(),outboxEvent.getOrderId(), outboxEvent.getOrderDetails().stream().map(item->new OrderDetail(item.getVariantId(), item.getAmount())).toList()
        );
        Message<vn.uit.edu.msshop.order.domain.event.inventory.OrderCreated > message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC).build();

        try {
        inventoryOrderCreatedTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                orderCreatedInventoryOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //log.error("Fail, wait 5 seconds");
            }
        });
    }catch(Exception e) {
        //System.out.println("Kafka is dead");
    e.printStackTrace();
    }
    }

    
    

    @Override
    public void publishIncreaseSoldCountEvent(IncreaseSoldCountEventsDocument outboxEvent) {
         IncreaseSoldCountEvents event = new IncreaseSoldCountEvents(outboxEvent.getEventId(), outboxEvent.getDetails().stream().map(item->new IncreaseSoldCountDetail(item.getVariantId(), item.getAmount())).toList());
         Message<IncreaseSoldCountEvents> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PRODUCT_TOPIC).build();
         try {
        increaseSoldCountTemplate.send(message)  
        .whenComplete((result,ex)->{
            if(ex==null) {
                //System.out.println("Send event with id "+outboxEvent.getEventId());
                increaseSoldCountEventOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //System.out.println("Send event fail");
                //log.error("Fail, wait 5 seconds");
            }
        })
        ;
    }
    catch(Exception e) {
    //System.out.println("Kafka is dead");
    //e.printStackTrace();
}
    }

    @Override
    public void publishOnlinePaymentCancelledEvent(OnlinePaymentCancelledDocument outboxEvent) {
        OnlinePaymentCancelled event = new OnlinePaymentCancelled(outboxEvent.getEventId(), outboxEvent.getOrderId());
        Message<OnlinePaymentCancelled> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PAYMENT_STATUS_TOPIC).build();
        try {
        onlinePaymentCancelledTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                onlinePaymentCancelledOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                //log.error("Fail, wait 5 seconds");
            }
        });
    }catch(Exception e) {
        //System.out.println("Kafka is dead");
    e.printStackTrace();
    }
    }

    @Override
    public void publishOrderUpdatedEvent(OrderUpdatedEventDocument event) {
        OrderUpdatedEvent orderUpdatedEvent= new OrderUpdatedEvent(event.getEventId(), event.getOrderId(), event.getDetails(), event.getStatus(), event.getUserId(), event.getOriginPrice(), event.getShippingFee(), event.getDiscount(), event.getTotalPrice(), event.getCurrency(), event.getPaymentMethod(), event.getPaymentStatus(),event.getEmail(), event.getOldStatus());
         Message<OrderUpdatedEvent> message = MessageBuilder.withPayload(orderUpdatedEvent).setHeader(KafkaHeaders.TOPIC, ORDER_CREATED_TOPIC).build();
         try {
        orderShippedTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                //System.out.println("Send event with id "+event.getEventId());
               orderUpdatedOutboxPublisher.markAsSent(event);
            }
            else {
                //System.out.println("Send event fail");
                //log.error("Fail, wait 5 seconds");
            }
        })
        ;
    }
    catch(Exception e) {
    //System.out.println("Kafka is dead");
    //e.printStackTrace();
}
    }


}
