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
import vn.edu.uit.msshop.product.shared.event.document.ProductCreatedDocument;
import vn.edu.uit.msshop.product.shared.event.domain.ProductCreated;
import vn.edu.uit.msshop.product.shared.event.domain.VariantCreated;
import vn.edu.uit.msshop.product.shared.event.repository.ProductCreatedRepository;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCreatedOutboxPublisher {
    
    private final ProductCreatedRepository productCreatedRepo;
    private final KafkaTemplate<String, ProductCreated> kafkaTemplate;
    private static final String PUBLISH_TOPIC="product-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<ProductCreatedDocument> pendingEvents =productCreatedRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (ProductCreatedDocument event : pendingEvents) {
            try {
                List<VariantCreated> variantCreateds = event.getVariantCreateds().stream().map(item->new VariantCreated(item.getVariantId(), event.getProductId(), event.getProductName(), item.getPrice(), item.getTraits(), item.getImageKey())).toList();
                ProductCreated productCreated = new ProductCreated(event.getEventId(), event.getProductId(), event.getProductName(), variantCreateds);
                kafkaTemplate.send(PUBLISH_TOPIC, productCreated)
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

    private void updateStatus(ProductCreatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        productCreatedRepo.save(event);
    }
    private void handleFailure(ProductCreatedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(ProductCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    productCreatedRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
