package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantProductId(
        UUID value)
        implements Comparable<VariantProductId> {
    public VariantProductId {
        if (value == null) {
            throw new DomainException("Variant product ID must NOT be null");
        }
    }

    @Override
    public int compareTo(
            final VariantProductId other) {
        return this.value.compareTo(other.value);
    }
}
