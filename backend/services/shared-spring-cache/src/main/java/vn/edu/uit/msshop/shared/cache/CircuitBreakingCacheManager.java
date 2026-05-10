package vn.edu.uit.msshop.shared.cache;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CircuitBreakingCacheManager implements CacheManager {
    private final CacheManager delegate;
    private final CircuitBreaker circuitBreaker;

    @Override
    public @Nullable Cache getCache(
            final String name) {
        final var cache = this.delegate.getCache(name);
        if (cache == null) {
            return null;
        }

        return new CircuitBreakingCache(cache, this.circuitBreaker);
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.delegate.getCacheNames();
    }
}
