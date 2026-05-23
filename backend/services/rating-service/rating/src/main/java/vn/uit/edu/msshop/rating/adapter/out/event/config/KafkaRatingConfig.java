package vn.uit.edu.msshop.rating.adapter.out.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import vn.uit.edu.msshop.rating.adapter.out.event.RatingKafkaProducerListener;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingIntegrationEvent;

@Configuration
public class KafkaRatingConfig {
    public static final String TOPIC_NAME = "rating-topic";

    private static final int RATING_TOPIC_PARTITION_COUNT = 3;
    private static final int RATING_TOPIC_REPLICATION_FACTOR = 1;

    @Bean
    KafkaTemplate<String, RatingIntegrationEvent> kafkaTemplate(
            ProducerFactory<String, RatingIntegrationEvent> producerFactory,
            RatingKafkaProducerListener listener) {
        final var template = new KafkaTemplate<String, RatingIntegrationEvent>(producerFactory);
        template.setProducerListener(listener);
        return template;
    }

    @Bean
    NewTopic variantTopic() {
        return TopicBuilder
                .name(TOPIC_NAME)
                .partitions(RATING_TOPIC_PARTITION_COUNT)
                .replicas(RATING_TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
