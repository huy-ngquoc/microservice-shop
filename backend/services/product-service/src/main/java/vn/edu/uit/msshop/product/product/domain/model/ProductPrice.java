package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductPrice(
        long value) {
    public ProductPrice {
        if (value < 0) {
            throw new DomainException("Price must not be negative");
        }
    }
}
