package vn.edu.uit.msshop.account.application.dto.command;

import vn.edu.uit.msshop.account.application.common.Change;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountEmail;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountName;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountRole;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountStatus;

public record UpdateAccountCommand(AccountId accountId, Change<AccountName> accountName, Change<AccountEmail> email, Change<AccountRole> role, Change<AccountStatus> status) {

}
