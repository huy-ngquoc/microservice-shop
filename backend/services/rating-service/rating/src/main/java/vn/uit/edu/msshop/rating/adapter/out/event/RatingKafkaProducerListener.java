package vn.uit.edu.msshop.rating.adapter.out.event;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingIntegrationEvent;

@Component
@Slf4j
public class RatingKafkaProducerListener
        implements ProducerListener<String, RatingIntegrationEvent> {
    @Override
    public void onSuccess(
            ProducerRecord<String, RatingIntegrationEvent> producerRecord,
            RecordMetadata recordMetadata) {
        log.debug("Sent event successfully: {} | Offset: {}",
                producerRecord.value().getClass().getSimpleName(),
                recordMetadata.offset());
    }

    @Override
    public void onError(
            ProducerRecord<String, RatingIntegrationEvent> producerRecord,
            @Nullable
            RecordMetadata recordMetadata,
            Exception exception) {
        final var eventName = producerRecord.value().getClass().getSimpleName();

        if (recordMetadata != null) {
            log.error("Failed to send event: {} | Topic: {} | Partition: {}",
                    eventName,
                    recordMetadata.topic(),
                    recordMetadata.partition(),
                    exception);

            return;
        }

        log.error("Failed to send event: {} | Target Topic: {} | Key: {}",
                eventName,
                producerRecord.topic(),
                producerRecord.key(),
                exception);
    }
}
