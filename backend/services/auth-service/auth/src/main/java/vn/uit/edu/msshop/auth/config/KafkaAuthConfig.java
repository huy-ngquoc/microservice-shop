package vn.uit.edu.msshop.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaAuthConfig {
    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("account-topic").partitions(3)
                .replicas(1)   
                .build();
    }
}
