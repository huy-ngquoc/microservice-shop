package vn.uit.edu.msshop.account.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.out.event.documents.RollbackImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.repositories.RollbackImageEventDocumentRepository;
import vn.uit.edu.msshop.account.domain.event.kafka.RollbackImageEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class RollbackImageEventOutboxPublisher {
    private final RollbackImageEventDocumentRepository rollbackImageEventRepo;
    private static final String PUBLISH_TOPIC="image-account-topic";
    private final KafkaTemplate<String, RollbackImageEvent> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<RollbackImageEventDocument> pendingEvents = rollbackImageEventRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (RollbackImageEventDocument event : pendingEvents) {
            try {
                RollbackImageEvent rollbackImageEvent = new RollbackImageEvent(event.getImagePublicId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, rollbackImageEvent)
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

    private void updateStatus(RollbackImageEventDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        rollbackImageEventRepo.save(event);
    }
    @Transactional
    public void markAsSent(RollbackImageEventDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(RollbackImageEventDocument event, String error) {
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
    
    rollbackImageEventRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
