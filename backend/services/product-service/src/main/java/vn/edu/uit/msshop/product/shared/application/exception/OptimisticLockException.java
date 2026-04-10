package vn.edu.uit.msshop.product.shared.application.exception;

public final class OptimisticLockException extends RuntimeException {
    private final long expectedVersion;
    private final long currentVersion;

    public OptimisticLockException(
            long expected,
            long current) {
        super("Version mismatch (expected: " + expected + ", current: " + current + ")");
        this.expectedVersion = expected;
        this.currentVersion = current;
    }

    public long getExceptedVersion() {
        return this.expectedVersion;
    }

    public long getCurrentVersion() {
        return this.currentVersion;
    }
}
