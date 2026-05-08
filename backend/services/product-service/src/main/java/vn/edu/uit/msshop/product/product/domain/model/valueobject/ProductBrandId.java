package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductBrandId(
        UUID value) implements Comparable<ProductBrandId> {
    public ProductBrandId {
        if (value == null) {
            throw new DomainException("Product brand ID is null");
        }
    }

    @Override
    public int compareTo(
            final ProductBrandId other) {
        return this.value.compareTo(other.value);
    }
}
