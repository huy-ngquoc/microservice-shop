package vn.uit.edu.msshop.account.application.exception;

import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(AccountId accountId) {

        super("Account with id "+accountId.value()+" does not exist");
    }
}
