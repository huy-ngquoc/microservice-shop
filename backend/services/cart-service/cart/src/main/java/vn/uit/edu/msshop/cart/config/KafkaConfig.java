package vn.uit.edu.msshop.cart.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("cart-topic").partitions(3)
                .replicas(1)
                .build();
    }
}
