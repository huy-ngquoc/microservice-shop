package vn.uit.edu.msshop.auth.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.application.port.in.DeleteAccountUseCase;
import vn.uit.edu.msshop.auth.domain.event.AccountId;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "account-topic-fail", groupId = "account-group")
public class AccountEventConsumer {
    private final DeleteAccountUseCase deleteAccountUseCase;

    @KafkaHandler
    public void handleAccountCreatedFail(AccountId accountId) {
        deleteAccountUseCase.deleteAccount(accountId);
        
    }
}
