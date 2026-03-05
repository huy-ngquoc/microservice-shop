package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductImage(
        ProductImageUrl url,
        ProductImageKey key,
        ProductImageSize size) {
    public ProductImage {
        if ((url == null) || (key == null) || (size == null)) {
            throw new DomainException("Image fields must not be null");
        }
    }
}
