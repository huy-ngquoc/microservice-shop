package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantProductId(
        UUID value) implements Comparable<VariantProductId> {
    public VariantProductId {
        if (value == null) {
            throw new DomainException("Variant product ID must NOT be null");
        }
    }

    public static @Nullable VariantProductId ofNullable(
            @Nullable
            final UUID value) {
        if (value == null) {
            return null;
        }

        return new VariantProductId(value);
    }

    @Override
    public int compareTo(
            final VariantProductId other) {
        return this.value.compareTo(other.value);
    }
}
