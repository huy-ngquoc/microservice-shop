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
import vn.uit.edu.payment.adapter.out.event.documents.CodPaymentCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.CodPaymentCreatedDocumentRepository;
import vn.uit.edu.payment.domain.event.CodPaymentCreated;

@Component
@Slf4j
@RequiredArgsConstructor
public class CodPaymentCreatedOutboxPublisher {
    private final CodPaymentCreatedDocumentRepository codPaymentCreatedDocumentRepo;
    private static final String PUBLISH_TOPIC="payment-online-topic";
    private final KafkaTemplate<String, CodPaymentCreated> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<CodPaymentCreatedDocument> pendingEvents = codPaymentCreatedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (CodPaymentCreatedDocument event : pendingEvents) {
            try {
                CodPaymentCreated codPaymentCreated= new CodPaymentCreated(event.getOrderId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, codPaymentCreated)
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

    private void updateStatus(CodPaymentCreatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        codPaymentCreatedDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(CodPaymentCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(CodPaymentCreatedDocument event, String error) {
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
    
    codPaymentCreatedDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
