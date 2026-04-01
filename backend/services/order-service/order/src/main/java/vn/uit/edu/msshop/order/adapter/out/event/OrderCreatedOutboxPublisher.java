package vn.uit.edu.msshop.order.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedOutboxPublisher {

    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final KafkaTemplate<String, OrderCreated> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OrderCreatedDocument> pendingEvents =orderCreatedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OrderCreatedDocument event : pendingEvents) {
            try {
                OrderCreated orderCreated = new OrderCreated(event.getEventId(),event.getCurrency(), event.getOrderId(),event.getPaymentMethod(), event.getPaymentValue());
                kafkaTemplate.send(PUBLISH_TOPIC, orderCreated)
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

    private void updateStatus(OrderCreatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderCreatedDocumentRepo.save(event);
    }
    private void handleFailure(OrderCreatedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(OrderCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    orderCreatedDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
