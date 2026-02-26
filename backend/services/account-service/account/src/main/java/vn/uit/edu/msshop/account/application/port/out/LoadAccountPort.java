package vn.edu.uit.msshop.profile.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.account.domain.model.Account;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;

public interface LoadAccountPort {
    public Optional<Account> loadById(AccountId id);
}
