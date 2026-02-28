package vn.uit.edu.msshop.account.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.out.event.AccountEventPublisherAdapter;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAccountCommand;
import vn.uit.edu.msshop.account.application.exception.AccountNotFoundException;
import vn.uit.edu.msshop.account.application.port.in.UpdateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.event.AccountUpdate;
import vn.uit.edu.msshop.account.domain.model.Account;

@Service
@RequiredArgsConstructor
public class UpdateAccountService implements UpdateAccountUseCase {
    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountEventPublisherAdapter eventPublisherAdapter;

    @Override
    public void update(UpdateAccountCommand updateAccountCommand) {
        final var account = this.loadAccountPort.loadById(updateAccountCommand.accountId()).orElseThrow(()->new AccountNotFoundException(updateAccountCommand.accountId()));
        final var update = Account.UpdateInfo.builder().id(account.getId()).name(updateAccountCommand.accountName().apply(account.getName()))
        .email(updateAccountCommand.email().apply(account.getEmail())).password(updateAccountCommand.password().apply(account.getPassword())).role(updateAccountCommand.role().apply(account.getRole())).status(updateAccountCommand.status().apply(account.getStatus())).build();
        final var next = account.applyUpdateInfo(update);
        
        final var saved = this.saveAccountPort.save(next);
        System.out.println(saved.getName()+"ewrifghewfhew");
        this.eventPublisherAdapter.publish(new AccountUpdate(saved.getId()));

    }
    
}
