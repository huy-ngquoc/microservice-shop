package vn.edu.uit.msshop.product.brand.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record BrandId(
        UUID value) implements Comparable<BrandId> {
    public BrandId {
        if (value == null) {
            throw new DomainException("ID null");
        }
    }

    public static BrandId newId() {
        return new BrandId(UUID.randomUUID());
    }

    @Override
    public int compareTo(
            final BrandId other) {
        return this.value.compareTo(other.value);
    }
}
