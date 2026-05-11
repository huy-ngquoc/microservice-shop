package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record ProductVariantId(
        UUID value) implements Comparable<ProductVariantId> {
    public ProductVariantId {
        if (value == null) {
            throw new DomainException("Product variant ID is null");
        }
    }

    public static ProductVariantId newId() {
        return new ProductVariantId(UUIDs.newId());
    }

    @Override
    public int compareTo(
            final ProductVariantId other) {
        return this.value.compareTo(other.value);
    }
}
