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
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentSuccessRepository;
import vn.uit.edu.payment.domain.event.OnlinePaymentSuccess;


@Component
@Slf4j
@RequiredArgsConstructor
public class OnlinePaymentSuccessOutboxPublisher {
    private final OnlinePaymentSuccessRepository onlinePaymentSuccessRepo;
    private static final String PUBLISH_TOPIC="payment-online-topic";
    private final KafkaTemplate<String, OnlinePaymentSuccess> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<OnlinePaymentSuccessDocument> pendingEvents = onlinePaymentSuccessRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OnlinePaymentSuccessDocument event : pendingEvents) {
            try {
                OnlinePaymentSuccess onlinePaymentExpired = new OnlinePaymentSuccess(event.getOrderId(), event.getEventId(), event.getUserEmail());
                kafkaTemplate.send(PUBLISH_TOPIC, onlinePaymentExpired)
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

    private void updateStatus(OnlinePaymentSuccessDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        onlinePaymentSuccessRepo.save(event);
    }
    @Transactional
    public void markAsSent(OnlinePaymentSuccessDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(OnlinePaymentSuccessDocument event, String error) {
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
    
    onlinePaymentSuccessRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
