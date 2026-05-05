package vn.edu.uit.msshop.product.shared.event.publisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.event.document.VariantRestoreDocument;
import vn.edu.uit.msshop.product.shared.event.domain.VariantRestore;
import vn.edu.uit.msshop.product.shared.event.repository.VariantRestoreRepository;

@Component
@RequiredArgsConstructor
public class VariantRestoreOutboxPublisher {
  private final VariantRestoreRepository variantRestoreRepo;
  private final KafkaTemplate<String, VariantRestore> kafkaTemplate;
  private static final String PUBLISH_TOPIC = "product-topic";

  @Scheduled(fixedDelay = 5000)

  public void publishPendingEvents() {
    List<VariantRestoreDocument> pendingEvents =
        variantRestoreRepo.findTop50ByEventStatusOrderByCreatedAtAsc("PENDING");

    for (VariantRestoreDocument event : pendingEvents) {
      try {
        VariantRestore variantRestore =
            new VariantRestore(event.getEventId(), event.getVariantId());
        kafkaTemplate.send(PUBLISH_TOPIC, variantRestore).whenComplete((result, ex) -> {
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

  private void updateStatus(VariantRestoreDocument event, String status, @Nullable String error) {
    event.setEventStatus(status);
    event.setUpdatedAt(Instant.now());
    event.setLastError(error);
    variantRestoreRepo.save(event);
  }

  private void handleFailure(VariantRestoreDocument event, @Nullable String error) {
    int retries = event.getRetryCount() == null ? 0 : event.getRetryCount();
    if (retries >= 3) {
      updateStatus(event, "FAILED", "Max retries reached: " + error);
    } else {
      event.setRetryCount(retries + 1);
      updateStatus(event, "PENDING", error);
    }
  }

  @Transactional
  public void markAsSent(VariantRestoreDocument event) {
    updateStatus(event, "SENT", null);
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void cleanupOldEvents() {
    Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);

    variantRestoreRepo.deleteByEventStatusAndUpdatedAtBefore("SENT", threshold);

  }
}
