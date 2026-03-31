package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountCreatedOutboxPublisher {
    private final AccountCreatedDocumentRepository accountCreatedDocumentRepo;
    private static final String PUBLISH_TOPIC="account-topic";
    private final KafkaTemplate<String,AccountCreated> kafkaTemplate;
    /*
    UUID id,
    String name, 
    String email,
    String password,
    String role,
    String status,
    String shippingAddress,
    String phoneNumber,
    UUID eventId */
     @Scheduled(fixedDelay=5000)
    public void publishPendingEvents() {
        List<AccountCreatedDocument> pendingEvents = accountCreatedDocumentRepo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (AccountCreatedDocument event : pendingEvents) {
            try {
                AccountCreated accountCreated = new AccountCreated(event.getId(), event.getName(), event.getEmail(), 
                event.getPassword(), event.getRole(), event.getStatus(), event.getShippingAddress(), event.getPhoneNumber(), event.getEventId());
                kafkaTemplate.send(PUBLISH_TOPIC, accountCreated)
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

    private void updateStatus(AccountCreatedDocument event, String status, String error) {
        event.setEventStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        accountCreatedDocumentRepo.save(event);
    }
    @Transactional
    public void markAsSent(AccountCreatedDocument event) {
        updateStatus(event, "SENT", null);
    }
    private void handleFailure(AccountCreatedDocument event, String error) {
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
    
    accountCreatedDocumentRepo.deleteByStatusAndUpdatedAtBefore("SENT", threshold);
   
}
}
