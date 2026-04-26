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
    
    private DefaultRedisScript<Long> updateInventoryStatus;
    private DefaultRedisScript<Long> reserveAllScript;
    private DefaultRedisScript<Long> cancelAllScript;
    private DefaultRedisScript<Long> changeStatusAllScript;
    private DefaultRedisScript<Long> releaseStockAllScript;
    private DefaultRedisScript<Long> reverseShipAllScript;
    private DefaultRedisScript<Long> plusReserveQuantityScript;
    private DefaultRedisScript<Long> minusAllQuantityScript;

    public RedisConfig() {
        
        updateInventoryStatus=updateStatusScript();
        reserveAllScript = reserveAllScript();
        cancelAllScript= cancellAllScript();
        changeStatusAllScript= changeStatusAllScript();
        releaseStockAllScript=releaseStockAllScript();
        reverseShipAllScript=reverseShipAllScript();
        plusReserveQuantityScript=plusReserveAllScript();
        minusAllQuantityScript=minusAllQuantityScript();

    }
    public DefaultRedisScript<Long> minusAllQuantityScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("minus_quantity_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    public DefaultRedisScript<Long> plusReserveAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("plus_reserve_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }

    
    public DefaultRedisScript<Long> reverseShipAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("reverse_ship_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    public DefaultRedisScript<Long> releaseStockAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("release_stock_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    public DefaultRedisScript<Long> changeStatusAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("change_status_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    
    
    
    public DefaultRedisScript<Long> cancellAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("cancel_stock_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    
    

    
    
    public DefaultRedisScript<Long> reserveAllScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        
        script.setLocation(new ClassPathResource("reserve_all.lua"));
        
        script.setResultType(Long.class);
        return script;
    }
    
    public DefaultRedisScript<Long> updateStatusScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        // Đường dẫn tính từ thư mục src/main/resources
        script.setLocation(new ClassPathResource("change_status.lua"));
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
