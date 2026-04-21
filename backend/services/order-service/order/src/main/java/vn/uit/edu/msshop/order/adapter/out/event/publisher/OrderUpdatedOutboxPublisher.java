package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.domain.event.OrderUpdatedEvent;
/*
private UUID eventId;
    private UUID orderId;

    private List<OrderDetailEvent> details;
    
    private String status;
    
    private UUID userId;

    private long originPrice;

    private long shippingFee;

    private long discount;

    private long totalPrice;

    

    

    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String email; */
@Component
@RequiredArgsConstructor
public class OrderUpdatedOutboxPublisher {
    private final OrderUpdatedRepository orderUpdatedRepo;
    private final KafkaTemplate<String, OrderUpdatedEvent> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OrderUpdatedEventDocument> pendingEvents =orderUpdatedRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OrderUpdatedEventDocument event : pendingEvents) {
            try {
                
                OrderUpdatedEvent orderUpdatedEvent= new OrderUpdatedEvent(event.getEventId(), event.getOrderId(), event.getDetails(), event.getStatus(), event.getUserId(), event.getOriginPrice(), event.getShippingFee(), event.getDiscount(), event.getTotalPrice(), event.getCurrency(), event.getPaymentMethod(), event.getPaymentStatus(),event.getEmail(),event.getOldStatus());
                kafkaTemplate.send(PUBLISH_TOPIC, orderUpdatedEvent)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            
                            updateStatus(event, "SENT", null);
                        } else {
                            
                            handleFailure(event, ex.getMessage());
                        }
                    });
            } catch (Exception e) {
                handleFailure(event, e.getMessage());
            }
        }
    }

    private void updateStatus(OrderUpdatedEventDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderUpdatedRepo.save(event);
        
    }
    private void handleFailure(OrderUpdatedEventDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(OrderUpdatedEventDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    orderUpdatedRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
