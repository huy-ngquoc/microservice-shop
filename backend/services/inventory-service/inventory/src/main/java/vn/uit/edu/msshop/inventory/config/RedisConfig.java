package vn.uit.edu.msshop.inventory.config;


import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.Getter;

@Configuration
@Getter
public class RedisConfig {
    private DefaultRedisScript<Long> reserveStockScript;
    private DefaultRedisScript<Long> reserveShippingStockScript;
    private DefaultRedisScript<Long> cancelStockScript;
    private DefaultRedisScript<Long> releaseStockScript;

    public RedisConfig() {
        reserveStockScript=  reserveStockScript();
        reserveShippingStockScript= reserveShippingStockScript();
        cancelStockScript = cancelStockScript();
        releaseStockScript=releaseStockScript();
    }
    
    
    public DefaultRedisScript<Long> reserveStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("reserve_stock.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    
    public DefaultRedisScript<Long> cancelStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("cancel_stock.lua"));
        
        script.setResultType(Long.class);
        return script;
    } 

    public DefaultRedisScript<Long> releaseStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("release_stock.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    public DefaultRedisScript<Long> reserveShippingStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("reverse_ship.lua"));
        
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
