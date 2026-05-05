package vn.edu.uit.msshop.product.category.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record CategoryId(
        UUID value)
        implements Comparable<CategoryId> {
    public CategoryId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }

    @Override
    public int compareTo(
            final CategoryId other) {
        return this.value.compareTo(other.value);
    }
}
