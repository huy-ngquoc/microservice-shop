package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductBrandId(
        UUID value) {
    public ProductBrandId {
        if (value == null) {
            throw new DomainException("Product brand ID is null");
        }
    }
}
