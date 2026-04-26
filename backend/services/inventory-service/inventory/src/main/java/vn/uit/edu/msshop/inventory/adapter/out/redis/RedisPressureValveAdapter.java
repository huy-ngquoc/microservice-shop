package vn.uit.edu.msshop.inventory.adapter.out.redis;

import java.util.Map;

import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.application.port.out.RedisPressureValve;

@Component
@RequiredArgsConstructor
public class RedisPressureValveAdapter implements RedisPressureValve {
    private final RedisTemplate<String,Map<String,String>> redisTemplate;
    @Override
    public void relief() {
        Long size = redisTemplate.execute(RedisServerCommands::dbSize);
        if(size != null && size > 1000) {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
        }
    }

}
