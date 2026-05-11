package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record ProductId(
        UUID value) implements Comparable<ProductId> {
    public ProductId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static ProductId newId() {
        return new ProductId(UUIDs.newId());
    }

    @Override
    public int compareTo(
            final ProductId other) {
        return this.value.compareTo(other.value);
    }
}
