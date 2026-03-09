package vn.uit.edu.msshop.account.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.kafka.dto.AccountId;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventProducer {
    private final KafkaTemplate<String,AccountId> kafkaTemplate;
    public void sendAccountCreationFailEvent(AccountId accountId) {
        Message<AccountId> message = MessageBuilder.withPayload(accountId).setHeader(KafkaHeaders.TOPIC, "account-topic-fail").build();
        kafkaTemplate.send(message);
    }

}