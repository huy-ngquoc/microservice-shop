package vn.uit.edu.msshop.inventory.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.inventory.domain.event.ForceCancellOrder;
@Component
@RequiredArgsConstructor
@Slf4j
public class ForceCancellOrderOutboxPublisher {
    private final ForceCancellOrderDocumentRepository forceCancellOrderDocumentRepo;
    private static final String PUBLISH_TOPIC="inventory-order";
    private final KafkaTemplate<String, ForceCancellOrder> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<ForceCancellOrderDocument> pendingEvents = forceCancellOrderDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (ForceCancellOrderDocument event : pendingEvents) {
            try {
                ForceCancellOrder forceCancellOrder= new ForceCancellOrder(event.getOrderId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, forceCancellOrder)
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

    private void updateStatus(ForceCancellOrderDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        
        event.setLastError(error);
        forceCancellOrderDocumentRepo.save(event);
    }
    private void handleFailure(ForceCancellOrderDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(ForceCancellOrderDocument outboxEvent) {
        updateStatus(outboxEvent, "PENDING", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    forceCancellOrderDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
