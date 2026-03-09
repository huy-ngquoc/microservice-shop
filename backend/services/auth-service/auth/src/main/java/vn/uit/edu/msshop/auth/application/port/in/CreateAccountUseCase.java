package vn.uit.edu.msshop.auth.application.port.in;

import org.keycloak.representations.idm.UserRepresentation;

public interface CreateAccountUseCase {
    public void createAccount(UserRepresentation user, String role);
}
