package vn.uit.edu.msshop.image.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.domain.event.ImageDeleted;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;
import vn.uit.edu.msshop.image.domain.event.ImageUpdated;

@Component
@RequiredArgsConstructor
public class ImageEventPublisher implements PublishImageEventPort {
    private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<String,ImageDeleted> kafkaTemplateDeleted;
    private final KafkaTemplate<String,ImageRemoveSuccess> kafkaTemplateRemoveSuccess;

    @Override
    public void publish(ImageUpdated imageUpdated) {
        publisher.publishEvent(imageUpdated);
    }

    @Override
    public void publish(ImageDeleted imageDeleted) {
        publisher.publishEvent(imageDeleted);
    }

    @Override
    public void publishDeleteImageEvent(ImageDeleted imageDeleted) {
        Message<ImageDeleted> message = MessageBuilder.withPayload(imageDeleted).setHeader(KafkaHeaders.TOPIC, "image-topic").build();
        kafkaTemplateDeleted.send(message);
    }

    @Override
    public void publishRemoveImageEvent(ImageRemoveSuccess imageRemoveSuccess) {
         Message<ImageRemoveSuccess> message = MessageBuilder.withPayload(imageRemoveSuccess).setHeader(KafkaHeaders.TOPIC, "image-topic").build();
         kafkaTemplateRemoveSuccess.send(message);
    }

}
