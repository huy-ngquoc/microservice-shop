package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountIdOutboxEventPublisher {
    private final AccountIdDocumentRepository accountIdDocumentRepo;
    private static final String PUBLISH_TOPIC="account-topic-fail";
    private final KafkaTemplate<String, AccountId> kafkaTemplate;
     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<AccountIdDocument> pendingEvents = accountIdDocumentRepo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (AccountIdDocument event : pendingEvents) {
            try {
                AccountId accountId = new AccountId(event.getAccontId(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, accountId)
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

    private void updateStatus(AccountIdDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        accountIdDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(AccountIdDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(AccountIdDocument event, String error) {
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
    
    accountIdDocumentRepo.deleteByStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
