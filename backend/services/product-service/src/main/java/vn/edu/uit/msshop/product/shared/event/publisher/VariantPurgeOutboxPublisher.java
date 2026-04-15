package vn.edu.uit.msshop.product.shared.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.event.document.VariantPurgeDocument;
import vn.edu.uit.msshop.product.shared.event.domain.VariantPurge;
import vn.edu.uit.msshop.product.shared.event.repository.VariantPurgeRepository;
@Component
@RequiredArgsConstructor
public class VariantPurgeOutboxPublisher {
     private final VariantPurgeRepository variantPurgeRepo;
    private final KafkaTemplate<String, VariantPurge> kafkaTemplate;
    private static final String PUBLISH_TOPIC="product-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<VariantPurgeDocument> pendingEvents = variantPurgeRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (VariantPurgeDocument event : pendingEvents) {
            try {
                VariantPurge variantPurge = new VariantPurge(event.getEventId(),event.getVariantId());
                kafkaTemplate.send(PUBLISH_TOPIC, variantPurge)
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

    private void updateStatus(VariantPurgeDocument event, String status, @Nullable String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        variantPurgeRepo.save(event);
    }
    private void handleFailure(VariantPurgeDocument event,@Nullable String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(VariantPurgeDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    variantPurgeRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
