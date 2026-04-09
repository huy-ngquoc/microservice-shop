package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTrait;

public record ProductVariantTrait(
        String value) {
    public static final int MAX_LENGTH = VariantTrait.MAX_LENGTH;
    public static final int MAX_RAW_LENGTH = VariantTrait.MAX_RAW_LENGTH;

    public ProductVariantTrait {
        if (value == null) {
            throw new DomainException("Option value CANNOT be null");
        }

        if (value.length() > MAX_RAW_LENGTH) {
            throw new DomainException("Option value wildly exceeds acceptable technical bounds");
        }

        if (value.isBlank()) {
            throw new DomainException("Option value CANNOT be blank");
        }

        value = Domains.getWhitespacePattern()
                .matcher(value.trim())
                .replaceAll(" ");

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Option value is too long");
        }

    }
}
