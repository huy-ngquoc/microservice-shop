package vn.uit.edu.msshop.account.application.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.domain.model.Account;



@Component
public class AccountViewMapper {
    public AccountView toView(Account account) {
        return new AccountView(account.getId().toString(),account.getName().value(),
         account.getEmail().value(), account.getPassword().value(), account.getRole().value(), account.getStatus().value());
    }
}
