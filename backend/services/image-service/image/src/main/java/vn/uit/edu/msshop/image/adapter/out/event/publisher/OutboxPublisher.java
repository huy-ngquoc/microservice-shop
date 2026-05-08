package vn.uit.edu.msshop.image.adapter.out.event.publisher;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.out.event.documents.ImageRemoveSuccessDocument;
import vn.uit.edu.msshop.image.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.image.adapter.out.event.repositories.ImageRemoveSuccessDocumentRepository;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;



@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final ImageRemoveSuccessDocumentRepository imageRemoveSuccessDocumentRepo;
    private final KafkaTemplate<String,ImageRemoveSuccess> kafkaTemplate;
    private final EventDocumentRepository eventDocumentRepo;
    private static final String PUBLISH_TOPIC = "image-topic";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<ImageRemoveSuccessDocument> pendingEvents = imageRemoveSuccessDocumentRepo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (ImageRemoveSuccessDocument event : pendingEvents) {
            try {
                ImageRemoveSuccess imageRemoveSuccess = new ImageRemoveSuccess(event.getEventId(), event.getUrl(), event.getPublicId()
                , event.getFileName(), event.getWidth(), event.getHeight(), event.getSize(), event.getObjectId());
                kafkaTemplate.send(PUBLISH_TOPIC, imageRemoveSuccess)
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

    private void updateStatus(ImageRemoveSuccessDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        imageRemoveSuccessDocumentRepo.save(event);
    }
    private void handleFailure(ImageRemoveSuccessDocument event, String error) {
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
    
    imageRemoveSuccessDocumentRepo.deleteByStatusAndUpdatedAtBefore("SENT", threshold);
   
}
    @Transactional
    public void markAsSent(ImageRemoveSuccessDocument event) {
        updateStatus(event, "SENT", null);
    }

    @Scheduled(cron="0 0 0 * * ?")
    public void cleanUpOldReceivedEvent() {
        Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);
        eventDocumentRepo.deleteByReceiveAtBefore(threshold);
    }
}