package vn.uit.edu.msshop.auth.application.port.out;

import vn.uit.edu.msshop.auth.domain.event.AccountId;

public interface DeleteAccountPort {
    public void deleteAccount(AccountId accountId);
}
