package vn.uit.edu.msshop.auth.application.port.in;

import vn.uit.edu.msshop.auth.domain.event.AccountId;

public interface DeleteAccountUseCase {
    public void deleteAccount(AccountId id);
}
