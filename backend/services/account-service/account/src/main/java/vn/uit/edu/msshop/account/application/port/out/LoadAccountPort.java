package vn.uit.edu.msshop.account.application.port.out;

import java.util.Optional;

import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;

public interface LoadAccountPort {
    public Optional<Account> loadById(AccountId id);
    public Account loadByUsername(AccountName name);
}
