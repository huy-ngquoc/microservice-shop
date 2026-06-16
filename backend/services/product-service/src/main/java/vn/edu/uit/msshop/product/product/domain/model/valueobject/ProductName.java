package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.product.ProductNameConstraints;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductName(
        String value) {

    public static final int MAX_LENGTH = ProductNameConstraints.MAX_LENGTH;
    public static final int MAX_RAW_LENGTH = ProductNameConstraints.MAX_RAW_LENGTH;

    public ProductName {
        if (value == null) {
            throw new DomainException("Product name is null");
        }

        if (value.length() > MAX_RAW_LENGTH) {
            throw new DomainException("Product name wildly exceeds acceptable technical bounds");
        }

        value = Domains.getWhitespacePattern().matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Product name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Product name is too long");
        }
    }
}
