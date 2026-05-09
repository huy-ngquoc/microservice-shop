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
import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.OnlinePaymentExpiredDocumentRepository;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnlinePaymentExpiredOutboxPublisher {
    private final OnlinePaymentExpiredDocumentRepository onlinePaymentExpiredDocumentRepo;
    private static final String PUBLISH_TOPIC = "payment-online-topic";
    private final KafkaTemplate<String, OnlinePaymentExpired> kafkaTemplate;

    @Scheduled(
            fixedDelay = 5000)
    public void publishPendingEvents() {
        List<OnlinePaymentExpiredDocument> pendingEvents = onlinePaymentExpiredDocumentRepo
                .findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (OnlinePaymentExpiredDocument event : pendingEvents) {
            try {
                OnlinePaymentExpired onlinePaymentExpired = new OnlinePaymentExpired(event.getOrderId(),
                        event.getEventId(), event.getUserEmail(), event.getUserId());
                kafkaTemplate.send(PUBLISH_TOPIC, onlinePaymentExpired)
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
            OnlinePaymentExpiredDocument event,
            String status,
            String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        onlinePaymentExpiredDocumentRepo.save(event);
    }

    @Transactional
    public void markAsSent(
            OnlinePaymentExpiredDocument event) {
        updateStatus(event, "SENT", null);
    }

    private void handleFailure(
            OnlinePaymentExpiredDocument event,
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

        onlinePaymentExpiredDocumentRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);

    }
}
