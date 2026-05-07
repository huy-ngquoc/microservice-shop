package vn.edu.uit.msshop.product.bootstrap.config.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "app.cache")
public record RedisCacheProperties(
        String keyPrefix,
        Duration brandTtl,
        Duration brandListTtl,
        Duration categoryTtl,
        Duration categoryListTtl,
        Duration productTtl,
        Duration productListTtl,
        Duration variantTtl,
        Duration variantListTtl) {
}
