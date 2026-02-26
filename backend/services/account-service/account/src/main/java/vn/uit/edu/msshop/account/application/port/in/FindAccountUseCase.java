package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;

public interface FindAccountUseCase {
    public void findAccountById(AccountId accountId);
}
