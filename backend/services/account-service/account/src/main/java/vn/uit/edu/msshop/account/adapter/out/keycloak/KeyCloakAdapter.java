package vn.uit.edu.msshop.account.adapter.out.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.port.out.CreateKeyCloakAccountPort;

@Component
@RequiredArgsConstructor
public class KeyCloakAdapter implements CreateKeyCloakAccountPort {

     private final Keycloak keycloak;
    private static final String REALM="ms_shop";
    @Override
    public Response createAccount(UserRepresentation user) {
        Response response = keycloak.realm(REALM).users().create(user);
        return response;
    }

}
