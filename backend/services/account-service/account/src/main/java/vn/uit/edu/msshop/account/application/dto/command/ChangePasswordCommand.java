package vn.edu.uit.msshop.account.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountPassword;

public record ChangePasswordCommand(AccountId accountId, AccountPassword oldPassword, AccountPassword nePassword) {

}
