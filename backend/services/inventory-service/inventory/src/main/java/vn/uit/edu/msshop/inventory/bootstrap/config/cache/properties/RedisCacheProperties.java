package vn.uit.edu.msshop.inventory.bootstrap.config.cache.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import vn.edu.uit.msshop.shared.cache.properties.CacheCircuitBreakerProperties;

@ConfigurationProperties(
        prefix = "app.cache")
public record RedisCacheProperties(
        String keyPrefix,
        Duration inventoryTtl,
        Duration inventoryListTtl,
        Duration inventoryByVariantTtl,
        CacheCircuitBreakerProperties circuitBreaker) {
}
