package vn.uit.edu.msshop.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("order-topic").build();
    }
    @Bean
    public NewTopic productTopic() {
        return TopicBuilder.name("order-product").build();
    }
    @Bean
    public NewTopic paymentCodTopic() {
        return TopicBuilder.name("payment-cod-topic").build();
    }
    @Bean
    public NewTopic orderInventoryTopic() {
        return TopicBuilder.name("order-inventory").build();
    }
}
