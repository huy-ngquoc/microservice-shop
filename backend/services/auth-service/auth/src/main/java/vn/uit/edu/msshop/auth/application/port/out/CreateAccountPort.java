package vn.uit.edu.msshop.auth.application.port.out;

import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.core.Response;


public interface CreateAccountPort {
    public Response createAccount(UserRepresentation user);
}
