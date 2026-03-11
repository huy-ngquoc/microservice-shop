package vn.edu.uit.msshop.product.variant.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantImageUrl(
        String value) {
    public VariantImageUrl {
        if (value == null) {
            throw new DomainException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new DomainException("imageUrl invalid");
        }
    }
}
