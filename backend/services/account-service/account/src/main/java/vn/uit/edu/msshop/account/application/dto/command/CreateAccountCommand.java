package vn.edu.uit.msshop.account.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.valueobject.AccountEmail;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountName;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountPassword;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountRole;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountStatus;

public record CreateAccountCommand(AccountId id, AccountName name, AccountEmail email, AccountPassword password, AccountRole role, AccountStatus status) {

}
