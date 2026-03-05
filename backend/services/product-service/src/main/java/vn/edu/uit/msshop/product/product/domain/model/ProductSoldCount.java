package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductSoldCount(
        int value) {
    public ProductSoldCount {
        if (value < 0) {
            throw new DomainException("Product sold count must NOT be negative");
        }
    }
}
