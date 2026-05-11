package vn.edu.uit.msshop.shared.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CircuitBreakingCache implements Cache {
    private final Cache delegate;
    private final CircuitBreaker circuitBreaker;

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return this.delegate.getNativeCache();
    }

    @Override
    public @Nullable ValueWrapper get(
            final Object key) {
        return this.execute(() -> this.delegate.get(key), null);
    }

    @Override
    public <T> @Nullable T get(
            final Object key,
            @Nullable
            final Class<T> type) {
        return this.execute(() -> this.delegate.get(key, type), null);
    }

    @Override
    public <T> @Nullable T get(
            final Object key,
            final Callable<T> valueLoader) {
        // Khi circuit OPEN, vẫn cần invoke valueLoader (= method body) để có data
        if (!this.circuitBreaker.tryAcquirePermission()) {
            try {
                return valueLoader.call();
            } catch (final Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
        }
        return this.measured(() -> this.delegate.get(key, valueLoader));
    }

    @Override
    public void put(
            final Object key,
            @Nullable
            final Object value) {
        if (this.circuitBreaker.tryAcquirePermission()) {
            this.measured(() -> {
                this.delegate.put(key, value);
                return null;
            });
        }
        // OPEN → skip put, không throw
    }

    @Override
    public void evict(
            final Object key) {
        if (this.circuitBreaker.tryAcquirePermission()) {
            this.measured(() -> {
                this.delegate.evict(key);
                return null;
            });
        }
    }

    @Override
    public void clear() {
        if (this.circuitBreaker.tryAcquirePermission()) {
            this.measured(() -> {
                this.delegate.clear();
                return null;
            });
        }
    }

    private <T> @Nullable T execute(
            final Supplier<T> action,
            @Nullable
            final T fallback) {
        if (!this.circuitBreaker.tryAcquirePermission()) {
            return fallback;
        }
        return this.measured(action);
    }

    private <T> @Nullable T measured(
            final Supplier<T> action) {
        final var start = System.nanoTime();
        try {
            final var result = action.get();
            this.circuitBreaker.onSuccess(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            return result;
        } catch (final RuntimeException e) {
            this.circuitBreaker.onError(System.nanoTime() - start, TimeUnit.NANOSECONDS, e);
            throw e;
        }
    }
}
