package vn.uit.edu.msshop.account.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.account.adapter.out.persistence.AccountJpaEntity;
import vn.uit.edu.msshop.account.domain.model.Account;

public interface SaveAccountPort {
    public Account save(Account account);
    public List<Account> saveAll(List<Account> accounts);
    public AccountJpaEntity saveAndReturnJpa(Account account);
}
