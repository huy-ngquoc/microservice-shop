package vn.uit.edu.msshop.account.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;
import vn.uit.edu.msshop.account.domain.event.kafka.DeleteOldImageEvent;
import vn.uit.edu.msshop.account.domain.event.kafka.RollbackImageEvent;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

@Component
@RequiredArgsConstructor
public class AccountEventPublisherAdapter implements PublishAccountEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String,AccountId> kafkaTemplate;
    private final KafkaTemplate<String,DeleteOldImageEvent> kafkaDeleteOldAvatarTemplate;
    private final KafkaTemplate<String,RollbackImageEvent> kafkaRollbackAvatarTemplate;
    
    private static final String PUBLISH_TOPIC = "account-topic-fail";
    private static final String PUBLISH_AVATAR_EVENT_TOPIC = "image-account-topic";
    

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

    @Override
    public void sendDeleteOldImageEvent(DeleteOldImageEvent event) {
        Message<DeleteOldImageEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_AVATAR_EVENT_TOPIC).build();
        kafkaDeleteOldAvatarTemplate.send(message);
    }

    @Override
    public void sendRollbackImageEvent(RollbackImageEvent event) {
        Message<RollbackImageEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_AVATAR_EVENT_TOPIC).build();
        kafkaRollbackAvatarTemplate.send(message);
    }

    

    

}
