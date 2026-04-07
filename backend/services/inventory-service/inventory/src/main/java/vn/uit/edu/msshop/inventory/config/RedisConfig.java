package vn.uit.edu.msshop.inventory.config;


import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public DefaultRedisScript<Long> reserveStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("reserve_stock.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    @Bean
public RedisTemplate<String, Map<String, String>> redisMapTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Map<String, String>> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    
    
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    template.afterPropertiesSet();
    return template;
}
}
