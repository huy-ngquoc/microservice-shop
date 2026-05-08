package vn.uit.edu.payment.adapter.out.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.event.documents.PaymentLinkCreatedDocument;
import vn.uit.edu.payment.adapter.out.event.repositories.PaymentLinkCreatedRepository;
import vn.uit.edu.payment.domain.event.PaymentLinkCreated;

@Component
@RequiredArgsConstructor
public class PaymentLinkCreatedOutboxPublisher {
    private final PaymentLinkCreatedRepository paymentLinkCreatedRepo;
    private static final String PUBLISH_TOPIC = "payment-online-topic";
    private final KafkaTemplate<String, PaymentLinkCreated> kafkaTemplate;

    @Scheduled(
            fixedDelay = 5000)
    public void publishPendingEvents() {
        List<PaymentLinkCreatedDocument> pendingEvents = paymentLinkCreatedRepo
                .findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

        for (PaymentLinkCreatedDocument event : pendingEvents) {
            try {
                PaymentLinkCreated paymentLinkCreated = new PaymentLinkCreated(event.getEventId(),
                        event.getPaymentLink(), event.getOrderId(), event.getUserEmail(), event.getUserId());
                kafkaTemplate.send(PUBLISH_TOPIC, paymentLinkCreated)
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
            PaymentLinkCreatedDocument event,
            String status,
            String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        paymentLinkCreatedRepo.save(event);
    }

    @Transactional
    public void markAsSent(
            PaymentLinkCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }

    private void handleFailure(
            PaymentLinkCreatedDocument event,
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

        paymentLinkCreatedRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);

    }
}
