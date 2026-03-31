package vn.uit.edu.msshop.account.adapter.in.web;
import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.adapter.out.event.AccountIdDocument;
import vn.uit.edu.msshop.account.adapter.out.event.AccountIdDocumentRepository;
import vn.uit.edu.msshop.account.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="account-topic", groupId="account-group")
public class AccountEventConsumer {
    private final CreateAccountUseCase createUseCase;
    private final AccountWebMapper mapper;
    private final PublishAccountEventPort producer;
    private final AccountIdDocumentRepository accountIdDocumentRepo;
    private final EventDocumentRepository eventDocumentRepo;
    @KafkaHandler
    @Transactional
    public void handleCreateAccount(AccountCreated accountCreated) {
        if(eventDocumentRepo.existsById(accountCreated.eventId())) {
            return;
        }
        try {
            CreateAccountCommand command = mapper.toCommand(accountCreated);
            createUseCase.create(command);
        }
        catch(Exception e) {
            e.printStackTrace();
            AccountIdDocument accountIdDocument = AccountIdDocument.builder().accontId(accountCreated.id())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null)
            .build();
            final var savedEvent=accountIdDocumentRepo.save(accountIdDocument);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                producer.sendAccountCreationFailEvent(savedEvent);
            }
        
        });
    }
    finally {
        eventDocumentRepo.save(new EventDocument(accountCreated.eventId(), Instant.now()));
    }
}
}