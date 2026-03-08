package vn.uit.edu.msshop.auth.kafka.consumer;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.domain.event.AccountId;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "account-topic", groupId = "account-group")
public class AccountEventConsumer {

    @KafkaHandler
    public void handleAccountCreatedFail(AccountId accountId) {

    }
}
