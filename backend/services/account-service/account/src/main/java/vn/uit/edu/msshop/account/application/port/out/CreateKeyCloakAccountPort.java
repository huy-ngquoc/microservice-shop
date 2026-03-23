package vn.uit.edu.msshop.account.application.port.out;

import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.core.Response;

public interface CreateKeyCloakAccountPort {
    public Response createAccount(UserRepresentation user);
}
