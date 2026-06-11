package vn.uit.edu.msshop.rating.adapter.out.event;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.rating.adapter.out.event.config.KafkaRatingConfig;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingCreatedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingDeletedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingUpdatedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingIntegrationEventPort;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingKafkaPublisherAdapter
        implements PublishRatingIntegrationEventPort {
    private final KafkaTemplate<String, RatingIntegrationEvent> kafkaTemplate;

    @Override
    public void publishCreated(
            final RatingCreatedIntegrationEvent event) {
                System.out.println("Send event");
        this.publish(event);
    }

    @Override
    public void publishUpdated(
            final RatingUpdatedIntegrationEvent event) {
        this.publish(event);
    }

    @Override
    public void publishDeleted(
            final RatingDeletedIntegrationEvent event) {
        this.publish(event);
    }

    // Result is handled globally by VariantKafkaProducerListener
    @SuppressWarnings("FutureReturnValueIgnored")
    private void publish(
            final RatingIntegrationEvent event) {
        final var producerRecord = new ProducerRecord<String, RatingIntegrationEvent>(
                KafkaRatingConfig.TOPIC_NAME,
                event.getAggregateId(),
                event);
        producerRecord.headers().add(
                "event-type",
                event.getClass().getSimpleName().getBytes(StandardCharsets.UTF_8));

        this.kafkaTemplate.send(producerRecord);
    }

    
}
