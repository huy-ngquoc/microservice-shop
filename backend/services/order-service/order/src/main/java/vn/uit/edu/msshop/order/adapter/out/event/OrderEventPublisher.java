package vn.uit.edu.msshop.order.adapter.out.event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderCreatedOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderCreatedSuccessOutboxPublisher;
import vn.uit.edu.msshop.order.adapter.out.event.publisher.OrderUpdatedOutboxPublisher;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderCreatedSuccess;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdatedEvent;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher implements PublishOrderEventPort{
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<String,OrderCreated> orderCreatedTemplate;
   
    
    private final KafkaTemplate<String, OrderCreatedSuccess> clearCartTemplate;
    
    
    private final KafkaTemplate<String, OrderUpdatedEventDocument> orderUpdatedEventDocumentTemplate;
    private static final String ORDER_CREATED_TOPIC = "order-topic";
    
    private static final String CLEAR_CART_TOPIC="cart-topic";
    
    private final OrderCreatedOutboxPublisher orderCreatedOutboxPublisher;
   
    private final OrderCreatedSuccessOutboxPublisher orderCreatedSuccessOutboxPublisher;
    
   
    
    
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
        System.out.println("Publish order createddddd");
        
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
    public void publishOrderUpdatedEvent(OrderUpdatedEventDocument event) {
        System.out.println("Publish eventttttttt order updated");
        OrderUpdatedEvent orderUpdatedEvent= new OrderUpdatedEvent(event.getEventId(), event.getOrderId(), event.getDetails(), event.getStatus(), event.getUserId(), event.getOriginPrice(), event.getShippingFee(), event.getDiscount(), event.getTotalPrice(), event.getCurrency(), event.getPaymentMethod(), event.getPaymentStatus(),event.getEmail(), event.getOldStatus());
         Message<OrderUpdatedEvent> message = MessageBuilder.withPayload(orderUpdatedEvent).setHeader(KafkaHeaders.TOPIC, ORDER_CREATED_TOPIC).build();
         try {
        orderUpdatedEventDocumentTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                System.out.println("Ackkkkkkkkkkkkkkkkkkkk");
                //System.out.println("Send event with id "+event.getEventId());
               orderUpdatedOutboxPublisher.markAsSent(event);
            }
            else {
                //System.out.println("Send event fail");
                log.error("Fail, wait 5 seconds");
            }
        })
        ;
    }
    catch(Exception e) {
    //System.out.println("Kafka is dead");
    e.printStackTrace();
}
    }


}
