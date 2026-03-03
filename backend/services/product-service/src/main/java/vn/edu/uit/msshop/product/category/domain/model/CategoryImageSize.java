package vn.edu.uit.msshop.product.category.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CategoryImageSize(
        int width,
        int height) {
    public CategoryImageSize {
        if ((width <= 0) || (height <= 0)) {
            throw new DomainException("invalid image size");
        }
    }
}
