package vn.uit.edu.msshop.account.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private final SyncAccountService syncService;
    @Value("${app.keycloak.admin.client-uuid}")
    private String clientUuid;

    @Override
    //@Transactional
    @Scheduled(fixedRate=5000)
    public void syncKeyCloak() {
        List<AccountOutboxEntity> pendingAccountOutboxEntities = accountOutboxRepo.findTop50ByIsCheckOrderByCreatedAtAsc(false);
        List<AccountOutboxEntity> toDelete= new ArrayList<>();
        List<Account> toSaves = new ArrayList<>();
        for(AccountOutboxEntity accountOutboxEntity: pendingAccountOutboxEntities) {
            syncService.sync(accountOutboxEntity, toSaves, toDelete);
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
        user.setFirstName(outboxEntity.getFirstName());
        user.setLastName(outboxEntity.getLastName());
        Map<String, List<String>> clientRolesMap = new HashMap<>();
 
    
        clientRolesMap.put(clientUuid, Collections.singletonList(outboxEntity.getUserRole()));
        user.setClientRoles(clientRolesMap);
        return user;
    }

}
