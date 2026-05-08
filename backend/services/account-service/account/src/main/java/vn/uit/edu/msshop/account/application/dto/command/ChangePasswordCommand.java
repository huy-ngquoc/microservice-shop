package vn.uit.edu.msshop.account.application.dto.command;

import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;


public record ChangePasswordCommand(AccountId accountId, AccountPassword oldPassword, AccountPassword nePassword) {

}
