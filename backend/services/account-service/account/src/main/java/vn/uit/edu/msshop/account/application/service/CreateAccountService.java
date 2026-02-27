package vn.uit.edu.msshop.account.application.service;



import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.domain.model.account.event.AccountCreated;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;
@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final SaveAccountPort savePort;
    private final PublishAccountEventPort eventPort;
    @Override
    public void create(CreateAccountCommand createAccountCommand) {
        final var draft = Account.Draft.builder().id(createAccountCommand.id()).name(createAccountCommand.name())
        .email(createAccountCommand.email()).password(createAccountCommand.password()).role(createAccountCommand.role()).status(createAccountCommand.status()).build();
        final var account = Account.create(draft);
        final var saved = this.savePort.save(account);
        this.eventPort.publish(new AccountCreated(saved.getId()));

    }

}
