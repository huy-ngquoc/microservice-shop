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
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.domain.VariantUpdate;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantUpdateOutboxPublisher {
    
    private final VariantUpdateRepository variantUpdateRepo;
    private final KafkaTemplate<String, VariantUpdate> kafkaTemplate;
    private static final String PUBLISH_TOPIC="product-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<VariantUpdateDocument> pendingEvents =variantUpdateRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (VariantUpdateDocument event : pendingEvents) {
            try {
                VariantUpdate variantUpdate = new VariantUpdate(event.getEventId(), event.getVariantId(), event.getProductId(), event.getProductName(),
            event.getPrice(), event.getTraits(), event.getImageKey());
                kafkaTemplate.send(PUBLISH_TOPIC, variantUpdate)
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

    private void updateStatus(VariantUpdateDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        variantUpdateRepo.save(event);
    }
    private void handleFailure(VariantUpdateDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(VariantUpdateDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    variantUpdateRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}