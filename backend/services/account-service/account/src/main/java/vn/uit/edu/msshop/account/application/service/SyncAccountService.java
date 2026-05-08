package vn.uit.edu.msshop.account.application.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntity;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntityRepository;
import vn.uit.edu.msshop.account.adapter.out.persistence.mapper.AccountEntityMapper;
import vn.uit.edu.msshop.account.application.port.out.CreateKeyCloakAccountPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;

@Service
@RequiredArgsConstructor
public class SyncAccountService {
    private final AccountOutboxEntityRepository accountOutboxRepo;
    private final SaveAccountPort saveAccountPort;
    private final AccountEntityMapper mapper;
    private final CreateKeyCloakAccountPort createKeyCloakPort;
    @Value("${app.keycloak.admin.client-uuid}")
    private String clientUuid;
    @Transactional
    public void sync(AccountOutboxEntity accountOutboxEntity, List<Account> toSaves, List<AccountOutboxEntity> toDelete) {
        if(accountOutboxEntity.getRetryCount()>=5) {
                System.out.println(accountOutboxEntity.getLastError());
                accountOutboxEntity.setCheck(true);
                accountOutboxRepo.save(accountOutboxEntity);
                accountOutboxEntity.getAccount().setStatus("CREATE_FAILED");
                saveAccountPort.save(mapper.toDomain(accountOutboxEntity.getAccount()));
                return;
            }
           // System.out.println("Call create accountttttt");
            try {
               Response respone= createKeyCloakPort.createAccount(toUserRepresentation(accountOutboxEntity), accountOutboxEntity.getUserRole());
            }
            catch(RuntimeException e) {
                e.printStackTrace();
                accountOutboxEntity.handleFailure(e.getMessage());
                accountOutboxRepo.save(accountOutboxEntity);
                return;
            }
            accountOutboxEntity.handleSuccess();
            final var accountEntity = accountOutboxEntity.getAccount();
            accountEntity.setStatus("ACTIVE");
            toSaves.add(mapper.toDomain(accountEntity));
            toDelete.add(accountOutboxEntity);
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
