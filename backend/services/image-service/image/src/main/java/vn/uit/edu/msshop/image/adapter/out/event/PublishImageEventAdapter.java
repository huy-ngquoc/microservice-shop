package vn.uit.edu.msshop.image.adapter.out.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;

@Service
@RequiredArgsConstructor
@Slf4j
/*private UUID eventId;
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    private long size;
    private UUID objectId; */
public class PublishImageEventAdapter implements PublishImageEventPort {
    private final KafkaTemplate<String, ImageRemoveSuccess> kafkaTemplate;
    private static final String PUBLISH_TOPIC = "image-topic";
    private final OutboxPublisher outboxPublisher;

    @Override
    public void publishImageRemoveSuccess(ImageRemoveSuccessDocument outboxEvent) {
        final var event = new ImageRemoveSuccess(outboxEvent.getEventId(), outboxEvent.getUrl(), outboxEvent.getPublicId(),
    outboxEvent.getFileName(), outboxEvent.getWidth(), outboxEvent.getHeight(), outboxEvent.getSize(), outboxEvent.getObjectId());
        Message message= MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();
        kafkaTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                outboxPublisher.markAsSent(outboxEvent);
            }
            else {
                log.error("Error sending event");
            }
        });
    }

}
