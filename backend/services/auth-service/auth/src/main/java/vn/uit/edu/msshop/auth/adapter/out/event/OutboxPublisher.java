package vn.uit.edu.msshop.auth.adapter.out.event;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.auth.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final AccountCreatedDocumentRepository accountCreatedDocumentRepo;
    private final PublishAccountEventPort publishPort;

    @Scheduled(fixedDelay=5000)
    
    public void publishPendingEvents() {
        List<AccountCreatedDocument> events = accountCreatedDocumentRepo.findByEventStatus("PENDING");
        
        for(AccountCreatedDocument event:events) {
            try {
                AccountCreated accountCreated = new AccountCreated(event.getAccountId().toString(), event.getName(), event.getEmail(), event.getPassword(),
                 event.getRole(), event.getStatus(), event.getShippingAddress(), event.getPhoneNumber());
                 publishPort.sendAccountCreateEvent(accountCreated);
                 accountCreatedDocumentRepo.delete(event);

            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
