package vn.uit.edu.payment.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentCancelledDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentCancelledDocumentRepository;
import vn.uit.edu.payment.domain.event.OnlinePaymentCancelled;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnlinePaymentCancelledOutboxPublisher {
    private final OnlinePaymentCancelledDocumentRepository onlinePaymentCancelledDocumentRepo;
    private static final String PUBLISH_TOPIC="payment-online-topic";
    private final KafkaTemplate<String, OnlinePaymentCancelled> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<OnlinePaymentCancelledDocument> pendingEvents = onlinePaymentCancelledDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OnlinePaymentCancelledDocument event : pendingEvents) {
            try {
                OnlinePaymentCancelled onlinePaymentCancelled = new OnlinePaymentCancelled(event.getOrderId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, onlinePaymentCancelled)
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

    private void updateStatus(OnlinePaymentCancelledDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        onlinePaymentCancelledDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(OnlinePaymentCancelledDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(OnlinePaymentCancelledDocument event, String error) {
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
    
    onlinePaymentCancelledDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
