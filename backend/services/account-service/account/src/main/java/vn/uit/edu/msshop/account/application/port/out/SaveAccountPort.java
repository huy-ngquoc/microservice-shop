package vn.uit.edu.msshop.account.application.port.out;

import vn.uit.edu.msshop.account.domain.model.Account;

public interface SaveAccountPort {
    public Account save(Account account);

}
