package vn.uit.edu.msshop.auth.adapter.out.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherAdapter implements PublishAccountEventPort {
    private final KafkaTemplate<String,AccountCreated> kafkaTemplate;
    @Override
    public void sendAccountCreateEvent(AccountCreated accountCreated) {
        Message<AccountCreated> message = MessageBuilder.withPayload(accountCreated).setHeader(KafkaHeaders.TOPIC, "account-topic").build();
        kafkaTemplate.send(message); 
    }

}
