package vn.uit.edu.msshop.auth.adapter.out.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.adapter.out.event.documents.AccountCreatedDocument;
import vn.uit.edu.msshop.auth.adapter.out.event.publisher.OutboxPublisher;
import vn.uit.edu.msshop.auth.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherAdapter implements PublishAccountEventPort {
    private final KafkaTemplate<String,AccountCreated> kafkaTemplate;
    private final OutboxPublisher outboxPublisher;
    @Override
    public void sendAccountCreateEvent(AccountCreatedDocument outboxEvent) {
        AccountCreated accountCreated = new AccountCreated(outboxEvent.getAccountId().toString(), outboxEvent.getName(), outboxEvent.getEmail(), 
                outboxEvent.getPassword(), outboxEvent.getRole(), outboxEvent.getStatus(), outboxEvent.getShippingAddress(), outboxEvent.getPhoneNumber(), outboxEvent.getEventId().toString());
        Message<AccountCreated> message = MessageBuilder.withPayload(accountCreated).setHeader(KafkaHeaders.TOPIC, "account-topic").build();
        kafkaTemplate.send(message)
        .whenComplete((result,ex)->{
            if(ex==null) {
                outboxPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        }); 
    }

}
