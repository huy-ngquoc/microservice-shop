package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantToUserPersistenceAdapter implements VariantToUserPort {
    private final StringRedisTemplate redisTemplate;
    private final String KEY_PREFIX = "variant_mapping";

    @Override
    public void addMapping(VariantId variantId, UserId userId) {
       String key = KEY_PREFIX+variantId.value().toString();
       redisTemplate.opsForSet().add(key, userId.value().toString());
    }

    @Override
    public void removeMapping(VariantId variantId, UserId userId) {
        String key = KEY_PREFIX+variantId.value().toString();
       redisTemplate.opsForSet().remove(key, userId.value().toString());
    }

    @Override
    public Set<String> getByVariantId(VariantId id) {
        String key = KEY_PREFIX + id.value().toString();
        return redisTemplate.opsForSet().members(key);
    }

}
