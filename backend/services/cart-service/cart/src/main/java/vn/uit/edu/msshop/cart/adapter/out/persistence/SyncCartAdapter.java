package vn.uit.edu.msshop.cart.adapter.out.persistence;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.out.persistence.mapper.CartRedisModelMapper;


@Component
@RequiredArgsConstructor
public class SyncCartAdapter {
    private final CartRedisModelMapper mapper;
    private final CartRepository cartRepo;
    private final RedisTemplate<String,Object> redisTemplate;
    private static final String KEY_PREFIX="cart_key:";
    @Scheduled(fixedRate=24*60000*60) 
    public void sync() {
        
    
    redisTemplate.execute((RedisConnection connection) -> {
        try (Cursor<byte[]> cursor = connection.scan(
                ScanOptions.scanOptions()
                        .match(KEY_PREFIX + "*")
                        .count(100) // Mỗi đợt lấy 100 keys
                        .build())) {
            
            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                // Lấy value từ key
                byte[] valueBytes = connection.get(keyBytes);
                if (valueBytes != null) {
                    
                    CartRedisModel model = (CartRedisModel) redisTemplate.getValueSerializer()
                                            .deserialize(valueBytes);
                    cartRepo.save(mapper.toDocument(model));
                }
            }
        }
        return null;
    });
    
    
    }
}
