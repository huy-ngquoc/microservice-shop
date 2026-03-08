package vn.uit.edu.msshop.account.kafka.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
import vn.uit.edu.msshop.account.kafka.dto.AccountCreated;

@Component
public class KafkaMessageMapper {
    public CreateAccountCommand toCommand(AccountCreated accountCreated) {
         final var id = new AccountId(accountCreated.id());
        final var name = new AccountName(accountCreated.name());
        final var email = new AccountEmail(accountCreated.email());
        final var password = new AccountPassword(accountCreated.password());
        final var role = new AccountRole(accountCreated.role());
        final var status = new AccountStatus("ACTIVE");
        return new CreateAccountCommand(id,name,email,password,role,status);
    }

}
