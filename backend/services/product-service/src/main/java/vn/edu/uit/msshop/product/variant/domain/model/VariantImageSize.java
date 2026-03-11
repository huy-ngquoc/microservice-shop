package vn.edu.uit.msshop.product.variant.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantImageSize(
        int width,
        int height) {
    public VariantImageSize {
        if ((width <= 0) || (height <= 0)) {
            throw new DomainException("Variant image size must be positive");
        }
    }
}
