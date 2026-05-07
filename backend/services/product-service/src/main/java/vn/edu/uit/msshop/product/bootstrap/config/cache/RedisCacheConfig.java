package vn.edu.uit.msshop.product.bootstrap.config.cache;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import vn.edu.uit.msshop.product.bootstrap.config.properties.RedisCacheProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisCacheConfig
        implements CachingConfigurer {
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler(true);
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory factory,
            RedisCacheProperties props,
            ObjectMapper objectMapper) {
        final var redisMapper = objectMapper.rebuild()
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType("vn.edu.uit.msshop")
                                .allowIfSubType("java.util")
                                .allowIfSubType("java.time")
                                .allowIfSubType("java.lang")
                                .build(),
                        DefaultTyping.NON_FINAL_AND_RECORDS,
                        JsonTypeInfo.As.PROPERTY)
                .build();

        final var jsonSerializer = new GenericJacksonJsonRedisSerializer(redisMapper);
        final var valueSerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer);

        final var stringSerializer = new StringRedisSerializer();
        final var keySerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(stringSerializer);

        final var base = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> props.keyPrefix() + ":" + cacheName + ":")
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
