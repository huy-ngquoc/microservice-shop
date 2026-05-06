package vn.edu.uit.msshop.product.bootstrap.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.ObjectMapper;
import vn.edu.uit.msshop.product.bootstrap.config.properties.RedisCacheProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisCacheConfig {
    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory factory,
            RedisCacheProperties props,
            ObjectMapper objectMapper) {
        final var jsonSerializer = new GenericJacksonJsonRedisSerializer(objectMapper);
        final var valueSerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer);

        final var stringSerializer = new StringRedisSerializer();
        final var keySerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(stringSerializer);

        final var base = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(valueSerializer)
                .serializeKeysWith(keySerializer);

        final var configs = Map.<String, RedisCacheConfiguration>ofEntries(
                Map.entry(CacheNames.BRAND, base.entryTtl(props.brandTtl())),
                Map.entry(CacheNames.BRAND_LIST, base.entryTtl(props.brandListTtl())),
                Map.entry(CacheNames.CATEGORY, base.entryTtl(props.categoryTtl())),
                Map.entry(CacheNames.CATEGORY_LIST, base.entryTtl(props.categoryListTtl())),
                Map.entry(CacheNames.PRODUCT, base.entryTtl(props.productTtl())),
                Map.entry(CacheNames.PRODUCT_LIST, base.entryTtl(props.productListTtl())),
                Map.entry(CacheNames.VARIANT, base.entryTtl(props.variantTtl())),
                Map.entry(CacheNames.VARIANT_LIST, base.entryTtl(props.variantListTtl())));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(base)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}
