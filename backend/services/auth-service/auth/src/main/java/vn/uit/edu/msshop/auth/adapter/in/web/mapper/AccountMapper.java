package vn.uit.edu.msshop.auth.adapter.in.web.mapper;

import java.util.Collections;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.auth.adapter.in.web.request.CreateAccountRequest;

@Component
public class AccountMapper {
    public UserRepresentation toUserRepresentation(CreateAccountRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.name());
        user.setEmail(request.email());
        user.setEnabled(true); 
        

       
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(request.password());
        user.setCredentials(Collections.singletonList(cred));
        return user;
    }
}
