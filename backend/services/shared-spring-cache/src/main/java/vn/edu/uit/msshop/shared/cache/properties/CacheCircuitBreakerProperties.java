package vn.edu.uit.msshop.shared.cache.properties;

import java.time.Duration;

public record CacheCircuitBreakerProperties(
        int slidingWindowSize,
        int minimumNumberOfCalls,
        int failureRateThreshold,
        Duration waitDurationInOpenState,
        int permittedNumberOfCallsInHalfOpenState) {
}
