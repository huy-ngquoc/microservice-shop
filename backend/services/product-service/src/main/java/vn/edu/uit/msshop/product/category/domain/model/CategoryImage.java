package vn.edu.uit.msshop.product.category.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CategoryImage(
        CategoryImageUrl url,
        CategoryImageKey key,
        CategoryImageSize size) {
    public CategoryImage {
        if ((url == null) || (key == null) || (size == null)) {
            throw new DomainException("Image fields must not be null");
        }
    }
}
