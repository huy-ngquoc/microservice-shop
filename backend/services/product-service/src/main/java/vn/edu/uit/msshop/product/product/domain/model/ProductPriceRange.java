package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductPriceRange(
        ProductPrice minPrice,
        ProductPrice maxPrice) {
    public ProductPriceRange {
        if (minPrice == null) {
            throw new DomainException("Min price is null");
        }

        if (maxPrice == null) {
            throw new DomainException("Max price is null");
        }

        if (minPrice.value() > maxPrice.value()) {
            throw new DomainException("Min price must not exceed max price");
        }
    }
}
