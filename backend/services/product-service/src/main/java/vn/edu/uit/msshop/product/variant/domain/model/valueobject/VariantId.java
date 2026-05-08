package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantId(
        UUID value) implements Comparable<VariantId> {
    public VariantId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static VariantId newId() {
        return new VariantId(UUID.randomUUID());
    }

    @Override
    public int compareTo(
            final VariantId other) {
        return this.value.compareTo(other.value);
    }
}
