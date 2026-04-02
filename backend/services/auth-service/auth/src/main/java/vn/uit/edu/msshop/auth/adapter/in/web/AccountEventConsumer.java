package vn.uit.edu.msshop.auth.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.auth.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.auth.application.port.in.DeleteAccountUseCase;
import vn.uit.edu.msshop.auth.domain.event.AccountId;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "account-topic-fail", groupId = "account-group")
public class AccountEventConsumer {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void handleAccountCreatedFail(AccountId accountId) {
        if(!eventDocumentRepo.existsById(accountId.eventId())) {
            deleteAccountUseCase.deleteAccount(accountId);
            final var newEvent = EventDocument.builder().eventId(accountId.eventId()).receiveAt(Instant.now()).build();
            eventDocumentRepo.save(newEvent);
        }
        
        
    }
}
