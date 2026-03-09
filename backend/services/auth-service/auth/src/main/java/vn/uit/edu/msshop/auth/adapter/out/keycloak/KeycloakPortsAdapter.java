package vn.uit.edu.msshop.auth.adapter.out.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.auth.application.port.out.CreateAccountPort;
import vn.uit.edu.msshop.auth.application.port.out.DeleteAccountPort;
import vn.uit.edu.msshop.auth.domain.event.AccountId;
@Component
@RequiredArgsConstructor
public class KeycloakPortsAdapter implements CreateAccountPort,DeleteAccountPort {
    private final Keycloak keycloak;
    private static final String REALM="ms_shop";
    @Override
    public Response createAccount(UserRepresentation user) {
        Response response = keycloak.realm(REALM).users().create(user);
        return response;
    }

    @Override
    public void deleteAccount(AccountId accountId) {
        keycloak.realm(REALM).users().get(accountId.value().toString()).remove();
    }

}
