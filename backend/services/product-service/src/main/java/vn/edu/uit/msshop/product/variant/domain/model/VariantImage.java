package vn.edu.uit.msshop.product.variant.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantImage(
        VariantImageUrl url,
        VariantImageKey key,
        VariantImageSize size) {
    public VariantImage {
        if ((url == null) || (key == null) || (size == null)) {
            throw new DomainException("Image fields must not be null");
        }
    }
}
