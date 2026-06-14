package vn.edu.uit.msshop.product.variant.adapter.out.event;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.adapter.out.event.config.VariantKafkaConfig;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantSoftDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantIntegrationEventPublicationPort;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantKafkaPublisherAdapter
        implements VariantIntegrationEventPublicationPort {
    private final KafkaTemplate<String, VariantIntegrationEvent> kafkaTemplate;

    @Override
    public void publishUpdated(
            final VariantUpdatedIntegrationEvent event) {
        this.publish(event);
    }

    @Override
    public void publishSoftDeleted(
            final VariantSoftDeletedIntegrationEvent event) {
        this.publish(event);
    }

    // Result is handled globally by VariantKafkaProducerListener
    @SuppressWarnings("FutureReturnValueIgnored")
    private void publish(
            final VariantIntegrationEvent event) {
        final var producerRecord = new ProducerRecord<String, VariantIntegrationEvent>(
                VariantKafkaConfig.TOPIC_NAME,
                event.getAggregateId(),
                event);
        producerRecord.headers().add(
                "event-type",
                event.getClass().getSimpleName().getBytes(StandardCharsets.UTF_8));

        this.kafkaTemplate.send(producerRecord);
    }
}
