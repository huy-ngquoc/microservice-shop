package vn.uit.edu.msshop.inventory.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;
import vn.uit.edu.msshop.inventory.adapter.out.event.repositories.InventoryUpdatedDocumentRepository;
import vn.uit.edu.msshop.inventory.domain.event.InventoryUpdated;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryUpdatedOutboxPublisher {
    private final InventoryUpdatedDocumentRepository inventoryUpdatedDocumentRepo;
    private static final String PUBLISH_TOPIC="inventory-product";
    private final KafkaTemplate<String, InventoryUpdated> kafkaTemplate;
    //@Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<InventoryUpdatedDocument> pendingEvents = inventoryUpdatedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (InventoryUpdatedDocument event : pendingEvents) {
            try {
                InventoryUpdated inventoryUpdated = new InventoryUpdated(event.getVariantId(), event.getNewQuantity(), event.getNewReservedQuantity(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, inventoryUpdated)
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

    private void updateStatus(InventoryUpdatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        inventoryUpdatedDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(InventoryUpdatedDocument outboxEvent) {
        updateStatus(outboxEvent,"SENT", null);
    }
    private void handleFailure(InventoryUpdatedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    inventoryUpdatedDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
