package vn.edu.uit.msshop.profile.application.port.out;

import vn.edu.uit.msshop.account.domain.model.Account;

public interface SaveAccountPort {
    public Account save(Account account);

}
