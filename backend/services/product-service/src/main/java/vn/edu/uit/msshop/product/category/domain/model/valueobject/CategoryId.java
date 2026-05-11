package vn.edu.uit.msshop.product.category.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record CategoryId(
        UUID value) implements Comparable<CategoryId> {
    public CategoryId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static CategoryId newId() {
        return new CategoryId(UUIDs.newId());
    }

    @Override
    public int compareTo(
            final CategoryId other) {
        return this.value.compareTo(other.value);
    }
}
