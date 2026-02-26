package vn.edu.uit.msshop.account.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.account.application.dto.query.AccountView;
import vn.edu.uit.msshop.account.domain.model.Account;

@Component
public class AccountViewMapper {
    public AccountView toView(Account account) {
        return new AccountView(account.getId().toString(),account.getName().value(),
         account.getEmail().value(), account.getRole().value(), account.getStatus().value());
    }
}
