package vn.edu.uit.msshop.shared.cache;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import vn.edu.uit.msshop.shared.cache.properties.CacheCircuitBreakerProperties;

public final class RedisCacheConfigSupport {
    private static final List<String> DEFAULT_ALLOWED_PACKAGES = List.of(
            "vn.edu.uit.msshop",
            "java.util",
            "java.time",
            "java.lang");

    private RedisCacheConfigSupport() {
    }

    /**
     * Builds an ObjectMapper configured with polymorphic typing for Redis JSON
     * serialization.
     * Default allowed packages are vn.edu.uit.msshop + standard Java packages.
     */
    public static ObjectMapper buildCacheObjectMapper(
            final ObjectMapper base) {
        return RedisCacheConfigSupport.buildCacheObjectMapper(
                base,
                DEFAULT_ALLOWED_PACKAGES);
    }

    /**
     * Builds an ObjectMapper configured with polymorphic typing for Redis JSON
     * serialization.
     */
    public static ObjectMapper buildCacheObjectMapper(
            final ObjectMapper base,
            final Iterable<String> allowedSubTypes) {
        var validatorBuilder = BasicPolymorphicTypeValidator.builder();
        for (final var pkg : allowedSubTypes) {
            validatorBuilder = validatorBuilder.allowIfSubType(pkg);
        }

        return base.rebuild()
                .activateDefaultTyping(
                        validatorBuilder.build(),
                        DefaultTyping.NON_FINAL_AND_RECORDS,
                        JsonTypeInfo.As.PROPERTY)
                .build();
    }

    /**
     * Builds a base RedisCacheConfiguration with key prefix and JSON serialization.
     * The cacheMapper must be the one returned by buildCacheObjectMapper.
     */
    public static RedisCacheConfiguration buildBaseConfig(
            final ObjectMapper cacheMapper,
            final String keyPrefix) {
        final var jsonSerializer = new GenericJacksonJsonRedisSerializer(cacheMapper);
        final var valueSerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer);

        final var stringSerializer = new StringRedisSerializer();
        final var keySerializer = RedisSerializationContext.SerializationPair
                .fromSerializer(stringSerializer);

        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> keyPrefix + ":" + cacheName + ":")
                .serializeValuesWith(valueSerializer)
                .serializeKeysWith(keySerializer);
    }

    /**
     * Builds a COUNT_BASED CircuitBreaker that records
     * RedisConnectionFailureException
     * and QueryTimeoutException as failures.
     */
    public static CircuitBreaker buildCacheCircuitBreaker(
            final String name,
            final CacheCircuitBreakerProperties props) {
        final var config = CircuitBreakerConfig.custom()
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(props.slidingWindowSize())
                .minimumNumberOfCalls(props.minimumNumberOfCalls())
                .failureRateThreshold(props.failureRateThreshold())
                .waitDurationInOpenState(props.waitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(props.permittedNumberOfCallsInHalfOpenState())
                .recordExceptions(
                        RedisConnectionFailureException.class,
                        QueryTimeoutException.class)
                .build();

        return CircuitBreaker.of(name, config);
    }

    /**
     * Builds an ApplicationListener that logs circuit breaker state transitions on
     * context refresh.
     */
    public static ApplicationListener<ContextRefreshedEvent> buildStateTransitionLogger(
            final CircuitBreaker cb,
            final Logger log) {
        return event -> cb.getEventPublisher()
                .onStateTransition(e -> log.warn(
                        "Redis cache circuit: from `{}` to `{}`",
                        e.getStateTransition().getFromState(),
                        e.getStateTransition().getToState()));
    }
}
