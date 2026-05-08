package vn.uit.edu.msshop.order.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import static org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder;
import org.springframework.data.redis.stream.Subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;

@Configuration
public class RedisConfig {
    private final RedisTemplate redisTemplate;
    public RedisConfig(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
        try {
    redisTemplate.opsForStream().createGroup("order_stream", "order-group");
} catch (Exception e) {
    // Nếu group tồn tại rồi thì thôi
}
    }
    @Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    
    // THAY ĐỔI Ở ĐÂY: Dùng StringSerializer cho Hash để tránh bị Base64/Extra Quotes
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new StringRedisSerializer()); 

    template.afterPropertiesSet();
    return template;
}
/*@Bean
public Subscription subscription(RedisConnectionFactory factory, OrderRedisConsumer consumer) {
    var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
            .builder()
            .pollTimeout(Duration.ofSeconds(1))
            .build(); // Không cần targetType, không cần objectMapper

    var container = StreamMessageListenerContainer.create(factory, options);


    var subscription=container.receive(
            Consumer.from("order-group", "worker-1"),
            StreamOffset.create("order_stream", ReadOffset.lastConsumed()),
            consumer // Consumer lúc này nhận Map thô
    );
    container.start(); 

    return subscription;
}*/
}
