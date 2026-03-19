package vn.edu.uit.msshop.product.product.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariantTrait(
        String value) {
    public static final int MAX_LENGTH = 30;
    public static final int MAX_RAW_LENGTH = (int) (MAX_LENGTH * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

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

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Option value is too long");
        }

    }
}
