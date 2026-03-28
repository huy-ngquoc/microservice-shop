package vn.uit.edu.msshop.auth.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
    private final AccountCreatedDocumentRepository accountCreatedDocumentRepo;
    private final KafkaTemplate<String,AccountCreated> kafkaTemplate;
    private final EventDocumentRepository eventDocumentRepo;
    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<AccountCreatedDocument> pendingEvents = accountCreatedDocumentRepo.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (AccountCreatedDocument event : pendingEvents) {
            try {
                AccountCreated accountCreated = new AccountCreated(event.getEventId().toString(),event.getAccountId().toString(),
                 event.getName(), event.getEmail(), event.getPassword(), event.getRole(), event.getStatus(), event.getShippingAddress(), event.getPhoneNumber());
                kafkaTemplate.send("account-topic", accountCreated)
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
        event.setStatus(status);
        event.setUpdatedAt(Instant.now());
        event.setLastError(error);
        accountCreatedDocumentRepo.save(event);
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

    @Scheduled(cron="0 0 0 * * ?")
    public void cleanUpOldReceivedEvent() {
        Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);
        eventDocumentRepo.deleteByReceiveAtBefore(threshold);
    }
}
