package vn.edu.uit.msshop.product.category.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record CategoryName(
        String value) {
    public static final int MAX_LENGTH = 40;
    public static final int MAX_RAW_LENGTH = (int) (MAX_LENGTH * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    public CategoryName {
        if (value == null) {
            throw new DomainException("Category name is null");
        }

        if (value.length() > MAX_RAW_LENGTH) {
            throw new DomainException("Category name wildly exceeds acceptable technical bounds");
        }

        if (value.isBlank()) {
            throw new DomainException("Category name is blank");
        }

        value = Domains.getWhitespacePattern()
                .matcher(value.trim())
                .replaceAll(" ");

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Category name is too long");
        }
    }
}
