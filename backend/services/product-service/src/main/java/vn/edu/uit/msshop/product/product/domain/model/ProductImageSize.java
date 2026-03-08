package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductImageSize(
        int width,
        int height) {
    public ProductImageSize {
        if ((width <= 0) || (height <= 0)) {
            throw new DomainException("invalid image size");
        }
    }
}
