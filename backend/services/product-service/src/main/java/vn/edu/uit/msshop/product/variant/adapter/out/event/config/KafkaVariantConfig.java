package vn.edu.uit.msshop.product.variant.adapter.out.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import vn.edu.uit.msshop.product.variant.adapter.out.event.VariantKafkaProducerListener;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantIntegrationEvent;

@Configuration
public class KafkaVariantConfig {
  public static final String TOPIC_NAME = "variant-topic";

  private static final int VARIANT_TOPIC_PARTITION_COUNT = 3;
  private static final int VARIANT_TOPIC_REPLICATION_FACTOR = 1;

  @Bean
  KafkaTemplate<String, VariantIntegrationEvent> kafkaTemplate(
      ProducerFactory<String, VariantIntegrationEvent> producerFactory,
      VariantKafkaProducerListener listener) {
    final var template = new KafkaTemplate<String, VariantIntegrationEvent>(producerFactory);
    template.setProducerListener(listener);
    return template;
  }

  @Bean
  NewTopic variantTopic() {
    return TopicBuilder.name(TOPIC_NAME).partitions(VARIANT_TOPIC_PARTITION_COUNT)
        .replicas(VARIANT_TOPIC_REPLICATION_FACTOR).build();
  }
}
