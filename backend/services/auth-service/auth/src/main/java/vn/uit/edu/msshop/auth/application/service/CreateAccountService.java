package vn.uit.edu.msshop.auth.application.service;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.auth.application.port.out.CreateAccountPort;
import vn.uit.edu.msshop.auth.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountService implements CreateAccountUseCase  {
    private final PublishAccountEventPort eventProducer;
    private final CreateAccountPort createPort;
    @Override
    public void createAccount(UserRepresentation user, String role, String shippingAddress, String phoneNumber ) {
        Response response = createPort.createAccount(user);

        if (response.getStatus() == 201) {
            
            String userId = CreatedResponseUtil.getCreatedId(response);
            AccountCreated event = new AccountCreated(userId, user.getUsername(), user.getEmail(),"", role, "ACTIVE", shippingAddress,phoneNumber);
            eventProducer.sendAccountCreateEvent(event);
        } else {
            System.out.println("Lỗi từ Keycloak: " + response.getStatus());
            System.out.println("Chi tiết: " + response.readEntity(String.class)); 
            throw new RuntimeException("Không thể tạo user trên Keycloak, lỗi: " + response.getStatus());
            
    
}
    }

}
