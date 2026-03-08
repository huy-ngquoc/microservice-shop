package vn.edu.uit.msshop.product.product.domain.model;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductCategoryId(
        UUID value) {
    public ProductCategoryId {
        if (value == null) {
            throw new DomainException("Product category ID is null");
        }
    }
}
