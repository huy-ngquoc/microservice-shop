package vn.uit.edu.msshop.account.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

@Component
@RequiredArgsConstructor
public class AccountEventPublisherAdapter implements PublishAccountEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String,AccountId> kafkaTemplate;
    private static final String PUBLISH_TOPIC = "account-topic-fail";
    

    @Override
    public void publish(AccountCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(AccountUpdate event) {
       this.publisher.publishEvent(event);
    }

    @Override
    public void sendAccountCreationFailEvent(AccountId accountId) {
         Message<AccountId> message = MessageBuilder.withPayload(accountId).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();
        kafkaTemplate.send(message);
    }

}
