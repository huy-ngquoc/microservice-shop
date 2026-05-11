package vn.uit.edu.msshop.inventory.bootstrap.config.cache;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import vn.edu.uit.msshop.shared.cache.CircuitBreakingCacheManager;
import vn.edu.uit.msshop.shared.cache.RedisCacheConfigSupport;
import vn.uit.edu.msshop.inventory.bootstrap.config.cache.properties.RedisCacheProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties.class)
@Slf4j
public class RedisCacheConfig
        implements CachingConfigurer {
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler(true);
    }

    @Bean
    CacheManager cacheManager(
            final RedisConnectionFactory factory,
            final RedisCacheProperties props,
            final ObjectMapper objectMapper,
            final CircuitBreaker redisCacheCircuitBreaker) {
        final var redisMapper = RedisCacheConfigSupport.buildCacheObjectMapper(objectMapper);
        final var base = RedisCacheConfigSupport.buildBaseConfig(
                redisMapper,
                props.keyPrefix());

        final var configs = Map.<String, RedisCacheConfiguration>ofEntries(
                Map.entry(CacheNames.INVENTORY, base.entryTtl(props.inventoryTtl())),
                Map.entry(CacheNames.INVENTORY_LIST, base.entryTtl(props.inventoryListTtl())),
                Map.entry(CacheNames.INVENTORY_BY_VARIANT, base.entryTtl(props.inventoryByVariantTtl())));

        final var redisManager = RedisCacheManager.builder(factory)
                .cacheDefaults(base)
                .withInitialCacheConfigurations(configs)
                .build();

        return new CircuitBreakingCacheManager(
                redisManager,
                redisCacheCircuitBreaker);
    }

    @Bean
    CircuitBreaker redisCacheCircuitBreaker(
            final RedisCacheProperties props) {
        return RedisCacheConfigSupport.buildCacheCircuitBreaker(
                props.keyPrefix() + "-cache",
                props.circuitBreaker());
    }

    @Bean
    ApplicationListener<ContextRefreshedEvent> cacheCircuitBreakerLogger(
            final CircuitBreaker redisCacheCircuitBreaker) {
        return RedisCacheConfigSupport.buildStateTransitionLogger(
                redisCacheCircuitBreaker,
                log);
    }
}
