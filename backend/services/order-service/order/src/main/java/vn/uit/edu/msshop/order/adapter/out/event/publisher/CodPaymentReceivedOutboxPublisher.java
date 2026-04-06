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
import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentReceivedDocumentRepository;
import vn.uit.edu.msshop.order.domain.event.CodPaymentReceived;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodPaymentReceivedOutboxPublisher {
    private final CodPaymentReceivedDocumentRepository codPaymentReceivedDocumentRepo;
    private final KafkaTemplate<String, CodPaymentReceived> kafkaTemplate;
    private static final String PUBLISH_TOPIC="payment-cod-topic";
   // @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<CodPaymentReceivedDocument> pendingEvents =codPaymentReceivedDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (CodPaymentReceivedDocument event : pendingEvents) {
            try {
                CodPaymentReceived codPaymentReceived = new CodPaymentReceived(event.getOrderId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, codPaymentReceived)
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

    private void updateStatus(CodPaymentReceivedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        codPaymentReceivedDocumentRepo.save(event);
    }
    private void handleFailure(CodPaymentReceivedDocument event, String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error); 
        }
    }
    @Transactional
    public void markAsSent(CodPaymentReceivedDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    codPaymentReceivedDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
