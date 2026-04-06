package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.OrderCreatedSuccess;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedSuccessOutboxPublisher {
     private final OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private final KafkaTemplate<String, OrderCreatedSuccess> kafkaTemplate;
    private static final String PUBLISH_TOPIC="cart-topic";
    //@Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OrderCreatedSuccessDocument> pendingEvents =orderCreatedSuccessDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OrderCreatedSuccessDocument event : pendingEvents) {
            try {
                OrderCreatedSuccess orderCreatedSuccess = new OrderCreatedSuccess(event.getEventId(),event.getUserId(), event.getVariantIds());
                kafkaTemplate.send(PUBLISH_TOPIC, orderCreatedSuccess)
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

    private void updateStatus(OrderCreatedSuccessDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        orderCreatedSuccessDocumentRepo.save(event);
    }
    private void handleFailure(OrderCreatedSuccessDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(OrderCreatedSuccessDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    orderCreatedSuccessDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
