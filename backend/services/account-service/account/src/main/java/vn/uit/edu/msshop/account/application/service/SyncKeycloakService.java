package vn.uit.edu.msshop.account.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntity;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntityRepository;
import vn.uit.edu.msshop.account.adapter.out.persistence.mapper.AccountEntityMapper;
import vn.uit.edu.msshop.account.application.port.in.SyncKeycloakUseCase;
import vn.uit.edu.msshop.account.application.port.out.CreateKeyCloakAccountPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;

@Service
@RequiredArgsConstructor
public class SyncKeycloakService implements SyncKeycloakUseCase {
    private final AccountOutboxEntityRepository accountOutboxRepo;
    private final CreateKeyCloakAccountPort createKeyCloakPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountEntityMapper mapper;

    @Override
    @Transactional
    @Scheduled(fixedRate=5000)
    public void syncKeyCloak() {
        List<AccountOutboxEntity> pendingAccountOutboxEntities = accountOutboxRepo.findTop50ByIsCheckOrderByCreatedAtAsc(false);
        List<AccountOutboxEntity> toDelete= new ArrayList<>();
        List<Account> toSaves = new ArrayList<>();
        for(AccountOutboxEntity accountOutboxEntity: pendingAccountOutboxEntities) {
            System.out.println("Call create accountttttt");
            try {
               createKeyCloakPort.createAccount(toUserRepresentation(accountOutboxEntity));
            }
            catch(RuntimeException e) {
                accountOutboxEntity.handleFailure(e.getMessage());
                continue;
            }
            accountOutboxEntity.handleSuccess();
            final var accountEntity = accountOutboxEntity.getAccount();
            accountEntity.setStatus("ACTIVE");
            toSaves.add(mapper.toDomain(accountEntity));
            toDelete.add(accountOutboxEntity);
        }
        saveAccountPort.saveAll(toSaves);
        accountOutboxRepo.deleteAll(toDelete);
    }
     private UserRepresentation toUserRepresentation(AccountOutboxEntity outboxEntity) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(outboxEntity.getUserName());
        user.setEmail(outboxEntity.getUserEmail());
        user.setEnabled(true);
        user.setId(outboxEntity.getUserId().toString());
       
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(outboxEntity.getPassword());
        user.setCredentials(Collections.singletonList(cred));
        Map<String, List<String>> clientRolesMap = new HashMap<>();
        String clientUuid = "fdaa89dd-7602-4f4b-8500-400870ee4b48"; 
    
        clientRolesMap.put(clientUuid, Collections.singletonList(outboxEntity.getUserRole()));
        user.setClientRoles(clientRolesMap);
        return user;
    }

}
