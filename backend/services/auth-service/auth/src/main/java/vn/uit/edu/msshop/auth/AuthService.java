package vn.uit.edu.msshop.auth;

import java.util.Collections;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.auth.domain.dto.request.CreateAccountRequest;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;
import vn.uit.edu.msshop.auth.kafka.producer.AccountEventProducer;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Keycloak keycloak;
    private final AccountEventProducer eventProducer;
    public void createUser(CreateAccountRequest dto) {
        
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.name());
        user.setEmail(dto.email());
        user.setEnabled(true);

       
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(dto.password());
        user.setCredentials(Collections.singletonList(cred));

        
        Response response = keycloak.realm("ms_shop").users().create(user);

        if (response.getStatus() == 201) {
            
            String userId = CreatedResponseUtil.getCreatedId(response);
            
            
            

            AccountCreated event = new AccountCreated(userId, dto.name(), dto.email(),"", dto.role(), "ACTIVE");
            eventProducer.sendAccountCreateEvent(event);
        } else {
            System.out.println("Lỗi từ Keycloak: " + response.getStatus());
    System.out.println("Chi tiết: " + response.readEntity(String.class)); // Dòng này sẽ hiện nguyên nhân cụ thể
            throw new RuntimeException("Không thể tạo user trên Keycloak, lỗi: " + response.getStatus());
            
    
}
        
    }
}
