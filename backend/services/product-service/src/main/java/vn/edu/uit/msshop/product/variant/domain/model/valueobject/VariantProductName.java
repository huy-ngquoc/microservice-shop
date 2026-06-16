package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.product.ProductNameConstraints;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantProductName(
        String value) {

    public static final int MAX_LENGTH = ProductNameConstraints.MAX_LENGTH;
    public static final int MAX_RAW_LENGTH = ProductNameConstraints.MAX_RAW_LENGTH;

    public VariantProductName {
        if (value == null) {
            throw new DomainException("Variant Product name is null");
        }

        value = Domains.getWhitespacePattern()
                .matcher(value.trim())
                .replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Variant Product name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Variant Product name is too long");
        }
    }
}
