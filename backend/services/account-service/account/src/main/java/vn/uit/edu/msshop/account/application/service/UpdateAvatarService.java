package vn.uit.edu.msshop.account.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.out.event.DeleteOldImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.DeleteOldImageEventDocumentRepository;
import vn.uit.edu.msshop.account.adapter.out.event.RollbackImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.RollbackImageEventDocumentRepository;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAvatarCommand;
import vn.uit.edu.msshop.account.application.exception.AccountNotFoundException;
import vn.uit.edu.msshop.account.application.port.in.UpdateAvatarUseCase;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.Avatar;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAvatarService implements UpdateAvatarUseCase {
    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final PublishAccountEventPort publishPort;
    private final DeleteOldImageEventDocumentRepository deleteOldImageEventDocumentRepo;
    private final RollbackImageEventDocumentRepository rollbackImageEventDocumentRepo;

    @Override
    @Transactional
    public void updateAvatar(UpdateAvatarCommand command) {
        try {
        
            final Account account = this.loadAccountPort.loadById(command.id()).orElseThrow(()->new AccountNotFoundException(command.id()));
            if(account.getAvatar()!=null) {
                final var savedOutboxEvent = deleteOldImageEventDocumentRepo.save(
                    DeleteOldImageEventDocument.builder().oldImagePublicId(account.getAvatar().publicId().value())
                    .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null)
            .build()
                );
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishPort.sendDeleteOldImageEvent(savedOutboxEvent);
            }
        });
                //publishPort.sendDeleteOldImageEvent(new DeleteOldImageEvent(account.getAvatar().publicId().value()));
            }
            Avatar avatar = new Avatar(command.publicId(),command.url(),command.imageSize());
            account.setAvatar(avatar);
            
            saveAccountPort.save(account);
    }
    catch(Exception e) {
        e.printStackTrace();
        final var savedOutboxEvent = rollbackImageEventDocumentRepo.save(
            RollbackImageEventDocument.builder()
            .imagePublicId(command.publicId().value())
            .eventStatus("PENDING")
            .retryCount(0)
            .createdAt(Instant.now())
            .updatedAt(null)
            .lastError(null)
            .build()
        );
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishPort.sendRollbackImageEvent(savedOutboxEvent);
            }
        });
        //publishPort.sendRollbackImageEvent(new RollbackImageEvent(command.publicId().value()));

    }



    }

}
