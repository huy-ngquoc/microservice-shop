package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductName(
        String value) {
    public static final int MAX_LENGTH = 40;

    public ProductName {
        if (value == null) {
            throw new DomainException("Product name is null");
        }

        value = Domains.getWhitespacePattern()
                .matcher(value.trim())
                .replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Product name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Product name is too long");
        }
    }
}
