package vn.edu.uit.msshop.account.application.exception;

import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(AccountId accountId) {

        super("Account with id "+accountId.value()+" does not exist");
    }
}
