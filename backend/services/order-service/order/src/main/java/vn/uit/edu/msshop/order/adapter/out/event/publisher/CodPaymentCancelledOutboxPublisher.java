package vn.uit.edu.msshop.order.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.CodPaymentCancelled;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodPaymentCancelledOutboxPublisher {
    
    private final CodPaymentCancelledDocumentRepository codPaymentCancelledDocumentRepo;
    private final KafkaTemplate<String, CodPaymentCancelled> kafkaTemplate;
    private static final String PUBLISH_TOPIC="payment-cod-topic";
    //@Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<CodPaymentCancelledDocument> pendingEvents =codPaymentCancelledDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (CodPaymentCancelledDocument event : pendingEvents) {
            try {
                CodPaymentCancelled orderCreated = new CodPaymentCancelled(event.getOrderId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, orderCreated)
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

    private void updateStatus(CodPaymentCancelledDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        codPaymentCancelledDocumentRepo.save(event);
    }
    private void handleFailure(CodPaymentCancelledDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(CodPaymentCancelledDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    codPaymentCancelledDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
