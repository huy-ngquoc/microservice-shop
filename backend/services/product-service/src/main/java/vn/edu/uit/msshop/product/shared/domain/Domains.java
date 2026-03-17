package vn.edu.uit.msshop.product.shared.domain;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public final class Domains {
    private Domains() {
    }

    public static final double RAW_LENGTH_TOLERANCE_FACTOR = 1.25;

    public static <T> T requireNonNull(
            @Nullable
            final T obj,

            final String msg) {
        if (obj == null) {
            throw new DomainException(msg);
        }

        return obj;
    }
}
