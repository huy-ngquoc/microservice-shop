package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record Amount(
        int value) {
    public Amount {
        if (value < 0) {
            throw new DomainException("Invalid amount");
        }
    }
}
