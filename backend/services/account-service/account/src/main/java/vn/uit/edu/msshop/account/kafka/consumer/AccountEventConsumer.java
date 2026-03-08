package vn.uit.edu.msshop.account.kafka.consumer;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.kafka.dto.AccountCreated;
import vn.uit.edu.msshop.account.kafka.dto.AccountId;
import vn.uit.edu.msshop.account.kafka.mapper.KafkaMessageMapper;
import vn.uit.edu.msshop.account.kafka.producer.AccountEventProducer;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="account-topic", groupId="account-group")
public class AccountEventConsumer {
    private final CreateAccountUseCase createUseCase;
    private final KafkaMessageMapper mapper;
    private final AccountEventProducer producer;
    @KafkaHandler
    public void handleCreateAccount(AccountCreated accountCreated) {
        try {
            CreateAccountCommand command = mapper.toCommand(accountCreated);
            createUseCase.create(command);
        }
        catch(Exception e) {
            producer.sendAccountCreationFailEvent(new AccountId(accountCreated.id()));
        }
    }
}
