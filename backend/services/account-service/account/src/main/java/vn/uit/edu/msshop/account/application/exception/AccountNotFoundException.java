package vn.uit.edu.msshop.account.application.exception;

import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.KeyCloakId;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(
            AccountId accountId) {

        super("Account with id " + accountId.value() + " does not exist");
    }

    public AccountNotFoundException(
            KeyCloakId keycloakId) {
        super("Account with keycloak id " + keycloakId.value() + " does not exist");
    }
}
