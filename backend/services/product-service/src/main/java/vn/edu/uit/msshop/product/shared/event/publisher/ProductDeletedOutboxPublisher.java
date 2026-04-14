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
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.event.document.ProductDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.domain.ProductDeleted;
import vn.edu.uit.msshop.product.shared.event.repository.ProductDeletedRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDeletedOutboxPublisher {
    
    private final ProductDeletedRepository productDeletedRepo;
    private final KafkaTemplate<String, ProductDeleted> kafkaTemplate;
    private static final String PUBLISH_TOPIC="product-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<ProductDeletedDocument> pendingEvents =productDeletedRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (ProductDeletedDocument event : pendingEvents) {
            try {
                
                ProductDeleted productDeleted = new ProductDeleted(event.getEventId(), event.getProductId());
                kafkaTemplate.send(PUBLISH_TOPIC, productDeleted)
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

    private void updateStatus(ProductDeletedDocument event, String status, @Nullable String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        productDeletedRepo.save(event);
    }
    private void handleFailure(ProductDeletedDocument event,@Nullable String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(ProductDeletedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    productDeletedRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
