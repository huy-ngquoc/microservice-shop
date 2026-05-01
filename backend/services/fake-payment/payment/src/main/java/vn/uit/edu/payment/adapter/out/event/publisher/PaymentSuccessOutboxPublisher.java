package vn.uit.edu.payment.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentSuccessDocumentRepository;
import vn.uit.edu.payment.domain.event.PaymentSuccess;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessOutboxPublisher {
    private final PaymentSuccessDocumentRepository paymentSuccessDocumentRepo;
    private static final String PUBLISH_TOPIC="payment-online-topic";
    private final KafkaTemplate<String, PaymentSuccess> kafkaTemplate;

     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<PaymentSuccessDocument> pendingEvents = paymentSuccessDocumentRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (PaymentSuccessDocument event : pendingEvents) {
            try {
                PaymentSuccess paymentSuccess = new PaymentSuccess(event.getEventId(), event.getOrderId());
                kafkaTemplate.send(PUBLISH_TOPIC, paymentSuccess)
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

    private void updateStatus(PaymentSuccessDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        paymentSuccessDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(PaymentSuccessDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(PaymentSuccessDocument event, String error) {
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
    
    paymentSuccessDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
