package vn.edu.uit.msshop.product.brand.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandLogoSize(
        int width,
        int height) {
    public BrandLogoSize {
        if ((width <= 0) || (height <= 0)) {
            throw new DomainException("invalid image size");
        }
    }
}
