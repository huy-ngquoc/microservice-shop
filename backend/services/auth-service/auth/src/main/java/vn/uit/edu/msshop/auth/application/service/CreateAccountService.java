package vn.uit.edu.msshop.auth.application.service;

import java.time.Instant;
import java.util.UUID;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.adapter.out.event.documents.AccountCreatedDocument;
import vn.uit.edu.msshop.auth.adapter.out.event.repositories.AccountCreatedDocumentRepository;
import vn.uit.edu.msshop.auth.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.auth.application.port.out.CreateAccountPort;
import vn.uit.edu.msshop.auth.application.port.out.PublishAccountEventPort;
/*
String name, 
    String email,
    String password,
    String role,
    String status,
    String shippingAddress,
    String phoneNumber
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountService implements CreateAccountUseCase  {
    private final PublishAccountEventPort eventProducer;
    private final CreateAccountPort createPort;
    private final AccountCreatedDocumentRepository accountCreatedDocumentRepo;
    @Override
    public void createAccount(UserRepresentation user, String role, String shippingAddress, String phoneNumber ) {
        Response response = createPort.createAccount(user);

        if (response.getStatus() == 201) {
            
            String userId = CreatedResponseUtil.getCreatedId(response);
            //AccountCreated event = new AccountCreated(userId, user.getUsername(), user.getEmail(),"", role, "ACTIVE", shippingAddress,phoneNumber);
            try {
                AccountCreatedDocument eventDocument = AccountCreatedDocument.builder().accountId(UUID.fromString(userId))
                .name(user.getUsername())
                .password("")
                .role(role)
                .status("ACTIVE")
                .shippingAddress(shippingAddress)
                .phoneNumber(phoneNumber).eventStatus("PENDING")
                .retryCount(0)
                .createdAt(Instant.now())
                .updatedAt(null)
                .lastError(null)
                .build();
                final var savedEvent = accountCreatedDocumentRepo.save(eventDocument);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventProducer.sendAccountCreateEvent(savedEvent);
            }
        });

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            //eventProducer.sendAccountCreateEvent(event);
        } else {
            System.out.println("Lỗi từ Keycloak: " + response.getStatus());
            System.out.println("Chi tiết: " + response.readEntity(String.class)); 
            throw new RuntimeException("Không thể tạo user trên Keycloak, lỗi: " + response.getStatus());
            
    
}
    }

}
