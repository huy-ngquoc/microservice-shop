package vn.uit.edu.msshop.account.application.dto.command;


import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.PhoneNumber;
import vn.uit.edu.msshop.account.domain.model.valueobject.ShippingAddress;

public record CreateAccountCommand(AccountName name, AccountEmail email, AccountPassword password, ShippingAddress shippingAddress,PhoneNumber phoneNumber, AccountRole role) {

}
