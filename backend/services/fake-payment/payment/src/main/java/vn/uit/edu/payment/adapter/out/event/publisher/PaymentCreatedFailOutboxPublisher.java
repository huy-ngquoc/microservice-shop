package vn.uit.edu.payment.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentCreatedFailDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentCreatedFailedRepository;
import vn.uit.edu.payment.domain.event.PaymentCreatedFail;

@Component
@RequiredArgsConstructor
public class PaymentCreatedFailOutboxPublisher {
    private final PaymentCreatedFailedRepository paymentCreatedFailRepo;
    private static final String PUBLISH_TOPIC = "payment-topic";
    private final KafkaTemplate<String, PaymentCreatedFail> kafkaTemplate;

    @Scheduled(
            fixedDelay = 5000)
    public void publishPendingEvents() {
        List<PaymentCreatedFailDocument> pendingEvents = paymentCreatedFailRepo
                .findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (PaymentCreatedFailDocument event : pendingEvents) {
            try {
                PaymentCreatedFail paymentCreatedFail = new PaymentCreatedFail(event.getEventId(), event.getOrderId(),
                        event.getUserId(), event.getUserEmail());
                kafkaTemplate.send(PUBLISH_TOPIC, paymentCreatedFail)
                        .whenComplete((
                                result,
                                ex) -> {
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

    private void updateStatus(
            PaymentCreatedFailDocument event,
            String status,
            String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        paymentCreatedFailRepo.save(event);
    }

    @Transactional
    public void markAsSent(
            PaymentCreatedFailDocument event) {
        updateStatus(event, "SENT", null);
    }

    private void handleFailure(
            PaymentCreatedFailDocument event,
            String error) {
        int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retries >= 3) {
            updateStatus(event, "FAILED", "Max retries reached: " + error);
        } else {
            event.setRetryCount(retries + 1);
            updateStatus(event, "PENDING", error);
        }
    }

    @Scheduled(
            cron = "0 0 0 * * ?")
    public void cleanupOldEvents() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);

        paymentCreatedFailRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);

    }
}
