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
public class PublishImageEventAdapter implements PublishImageEventPort {
    private final KafkaTemplate<String, ImageRemoveSuccess> kafkaTemplate;
    private static final String PUBLISH_TOPIC = "image-topic";

    @Override
    public void publishImageRemoveSuccess(ImageRemoveSuccess event) {
        Message message= MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();
        kafkaTemplate.send(message);
    }

}
