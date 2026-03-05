package vn.uit.edu.msshop.account.application.dto.command;


import vn.uit.edu.msshop.account.application.common.Change;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;


public record UpdateAccountCommand(AccountId accountId, Change<AccountName> accountName, Change<AccountEmail> email,Change<AccountPassword> password, Change<AccountRole> role, Change<AccountStatus> status) {

}
