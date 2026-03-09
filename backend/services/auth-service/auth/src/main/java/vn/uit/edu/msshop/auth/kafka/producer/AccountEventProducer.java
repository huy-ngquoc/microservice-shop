package vn.uit.edu.msshop.auth.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventProducer {
    private final KafkaTemplate<String,AccountCreated> kafkaTemplate;
    public void sendAccountCreateEvent(AccountCreated accountCreated) {
        Message<AccountCreated> message = MessageBuilder.withPayload(accountCreated).setHeader(KafkaHeaders.TOPIC, "account-topic").build();
        kafkaTemplate.send(message); 
    }

}
