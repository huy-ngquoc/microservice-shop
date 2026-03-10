package vn.uit.edu.msshop.account.adapter.in.web;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="account-topic", groupId="account-group")
public class AccountEventConsumer {
    private final CreateAccountUseCase createUseCase;
    private final AccountWebMapper mapper;
    private final PublishAccountEventPort producer;
    @KafkaHandler
    public void handleCreateAccount(AccountCreated accountCreated) {
        try {
            CreateAccountCommand command = mapper.toCommand(accountCreated);
            createUseCase.create(command);
        }
        catch(Exception e) {
            e.printStackTrace();
            producer.sendAccountCreationFailEvent(new AccountId(accountCreated.id()));
        }
    }
}