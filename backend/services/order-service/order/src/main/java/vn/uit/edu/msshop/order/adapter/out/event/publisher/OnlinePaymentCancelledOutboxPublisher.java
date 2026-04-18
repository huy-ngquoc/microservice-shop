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
import vn.uit.edu.msshop.order.adapter.out.event.documents.OnlinePaymentCancelledDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OnlinePaymentCancelledRepository;
import vn.uit.edu.msshop.order.domain.event.OnlinePaymentCancelled;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnlinePaymentCancelledOutboxPublisher {
    private final  OnlinePaymentCancelledRepository onlinePaymentCancelledRepo;
    private final KafkaTemplate<String, OnlinePaymentCancelled> kafkaTemplate;
    private static final String PUBLISH_TOPIC="order-product";
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<OnlinePaymentCancelledDocument> pendingEvents =onlinePaymentCancelledRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OnlinePaymentCancelledDocument event : pendingEvents) {
            try {
                final var onlinePaymentCancelled = new OnlinePaymentCancelled(event.getEventId(), event.getOrderId());
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
        onlinePaymentCancelledRepo.save(event);
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
    @Transactional
    public void markAsSent(OnlinePaymentCancelledDocument event) {
        updateStatus(event, "SENT", null);
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
    
    onlinePaymentCancelledRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
