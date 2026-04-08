package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountEventsDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.IncreaseSoldCountEventDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.IncreaseSoldCountDetail;
import vn.uit.edu.msshop.order.domain.event.IncreaseSoldCountEvents;
@Component
@RequiredArgsConstructor
public class IncreaseSoldCountEventOutboxPublisher {
    private final IncreaseSoldCountEventDocumentRepository increaseSoldCountEventDocumentRepository;
    private final KafkaTemplate<String, IncreaseSoldCountEvents> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-product";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<IncreaseSoldCountEventsDocument> pendingEvents =increaseSoldCountEventDocumentRepository.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (IncreaseSoldCountEventsDocument event : pendingEvents) {
            try {
                final var increaseSoldCount = new IncreaseSoldCountEvents(event.getEventId(), event.getDetails().stream().map(item->new IncreaseSoldCountDetail(item.getVariantId(), item.getAmount())).toList());
                kafkaTemplate.send(PUBLISH_TOPIC, increaseSoldCount)
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

    private void updateStatus(IncreaseSoldCountEventsDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        increaseSoldCountEventDocumentRepository.save(event);
    }
    private void handleFailure(IncreaseSoldCountEventsDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(IncreaseSoldCountEventsDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    increaseSoldCountEventDocumentRepository.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
