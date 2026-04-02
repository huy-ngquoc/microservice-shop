package vn.uit.edu.msshop.account.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.out.event.documents.AccountCreatedDocument;
import vn.uit.edu.msshop.account.adapter.out.event.documents.AccountIdDocument;
import vn.uit.edu.msshop.account.adapter.out.event.documents.DeleteOldImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.documents.RollbackImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.publisher.AccountCreatedOutboxPublisher;
import vn.uit.edu.msshop.account.adapter.out.event.publisher.AccountIdOutboxEventPublisher;
import vn.uit.edu.msshop.account.adapter.out.event.publisher.DeleteOldImageOutboxEventPublisher;
import vn.uit.edu.msshop.account.adapter.out.event.publisher.RollbackImageEventOutboxPublisher;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;
import vn.uit.edu.msshop.account.domain.event.kafka.DeleteOldImageEvent;
import vn.uit.edu.msshop.account.domain.event.kafka.RollbackImageEvent;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventPublisherAdapter implements PublishAccountEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String,AccountId> kafkaTemplate;
    private final KafkaTemplate<String,DeleteOldImageEvent> kafkaDeleteOldAvatarTemplate;
    private final KafkaTemplate<String,RollbackImageEvent> kafkaRollbackAvatarTemplate;
    private final KafkaTemplate<String,vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated> accountCreatedTemplate;
    private static final String PUBLISH_TOPIC = "account-topic-fail";
    private static final String PUBLISH_AVATAR_EVENT_TOPIC = "image-account-topic";
    private static final String PUBLISH_ACCOUNT_CREATED_TOPIC="account-profile";
    private final AccountIdOutboxEventPublisher accountIdOutboxEventPublisher;
    private final DeleteOldImageOutboxEventPublisher deleteOldImageOutboxEventPublisher;
    private final RollbackImageEventOutboxPublisher rollbackImageOutboxEventPublisher;
    private final AccountCreatedOutboxPublisher accountCreatedOutboxPublisher;

    @Override
    public void publish(AccountCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(AccountUpdate event) {
       this.publisher.publishEvent(event);
    }

    @Override
    public void sendAccountCreationFailEvent(AccountIdDocument outboxEvent) {
        AccountId accountId = new AccountId(outboxEvent.getAccontId(), outboxEvent.getEventId());
        Message<AccountId> message = MessageBuilder.withPayload(accountId).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();
        kafkaTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                accountIdOutboxEventPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        });
    }

    @Override
    public void sendDeleteOldImageEvent(DeleteOldImageEventDocument outboxEvent) {
        DeleteOldImageEvent event = new DeleteOldImageEvent(outboxEvent.getOldImagePublicId(), outboxEvent.getEventId());
        Message<DeleteOldImageEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_AVATAR_EVENT_TOPIC).build();
        kafkaDeleteOldAvatarTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                deleteOldImageOutboxEventPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        })
        ;
    }

    @Override
    public void sendRollbackImageEvent(RollbackImageEventDocument outboxEvent) {
        final var event = new RollbackImageEvent(outboxEvent.getImagePublicId(), outboxEvent.getEventId());
        Message<RollbackImageEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_AVATAR_EVENT_TOPIC).build();
        kafkaRollbackAvatarTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                rollbackImageOutboxEventPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        });
    }

    @Override
    public void sendAccountCreated(AccountCreatedDocument outboxEvent) {
        final var event = new vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated(outboxEvent.getId(), outboxEvent.getName(), outboxEvent.getEmail(), 
                outboxEvent.getPassword(), outboxEvent.getRole(), outboxEvent.getStatus(), outboxEvent.getShippingAddress(), outboxEvent.getPhoneNumber(), outboxEvent.getEventId());
        Message<vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,  PUBLISH_ACCOUNT_CREATED_TOPIC).build();
        accountCreatedTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                accountCreatedOutboxPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        });
    }

    

    

}
