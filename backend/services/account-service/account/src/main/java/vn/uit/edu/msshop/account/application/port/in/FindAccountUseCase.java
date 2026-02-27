package vn.uit.edu.msshop.account.application.port.in;



import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;

public interface FindAccountUseCase {
    public AccountView findAccountById(AccountId accountId);
}
