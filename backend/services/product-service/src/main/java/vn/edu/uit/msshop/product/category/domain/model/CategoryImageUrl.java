package vn.edu.uit.msshop.product.category.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CategoryImageUrl(
        String value) {
    public CategoryImageUrl {
        if (value == null) {
            throw new DomainException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new DomainException("imageUrl invalid");
        }
    }
}
