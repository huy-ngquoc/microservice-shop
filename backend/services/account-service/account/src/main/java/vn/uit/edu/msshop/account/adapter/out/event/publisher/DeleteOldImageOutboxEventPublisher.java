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
import vn.uit.edu.msshop.account.adapter.out.event.documents.DeleteOldImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.repositories.DeleteOldImageEventDocumentRepository;
import vn.uit.edu.msshop.account.domain.event.kafka.DeleteOldImageEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeleteOldImageOutboxEventPublisher {
    private final DeleteOldImageEventDocumentRepository deleteOldImageRepo;
    private static final String PUBLISH_TOPIC="image-account-topic";
    private final KafkaTemplate<String, DeleteOldImageEvent> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<DeleteOldImageEventDocument> pendingEvents = deleteOldImageRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (DeleteOldImageEventDocument event : pendingEvents) {
            try {
                DeleteOldImageEvent deleteOldImageEvent = new DeleteOldImageEvent(event.getOldImagePublicId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, deleteOldImageEvent)
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

    private void updateStatus(DeleteOldImageEventDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        deleteOldImageRepo.save(event);
    }
    @Transactional
    public void markAsSent(DeleteOldImageEventDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(DeleteOldImageEventDocument event, String error) {
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
    
    deleteOldImageRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
