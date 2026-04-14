package vn.edu.uit.msshop.product.shared.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.domain.VariantDeleted;
import vn.edu.uit.msshop.product.shared.event.repository.VariantDeletedRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantDeletedOutboxPublisher {
    
    private final VariantDeletedRepository variantDeletedRepo;
    private final KafkaTemplate<String, VariantDeleted> kafkaTemplate;
    private static final String PUBLISH_TOPIC="product-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<VariantDeletedDocument> pendingEvents =variantDeletedRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (VariantDeletedDocument event : pendingEvents) {
            try {
                VariantDeleted variantDeleted = new VariantDeleted(event.getEventId(), event.getVariantId());
                kafkaTemplate.send(PUBLISH_TOPIC, variantDeleted)
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

    private void updateStatus(VariantDeletedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        variantDeletedRepo.save(event);
    }
    private void handleFailure(VariantDeletedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(VariantDeletedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    variantDeletedRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}

