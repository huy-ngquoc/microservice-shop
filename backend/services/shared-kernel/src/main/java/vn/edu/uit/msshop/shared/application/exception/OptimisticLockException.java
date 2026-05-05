package vn.edu.uit.msshop.shared.application.exception;

import org.jspecify.annotations.Nullable;

import lombok.Getter;

@Getter
public final class OptimisticLockException extends RuntimeException {
    @Nullable
    private final Long expectedVersion;

    @Nullable
    private final Long currentVersion;

    public OptimisticLockException(
            @Nullable
            Long expected,

            @Nullable
            Long current) {
        super("Version mismatch (expected: " + expected + ", current: " + current + ")");
        this.expectedVersion = expected;
        this.currentVersion = current;
    }
}
