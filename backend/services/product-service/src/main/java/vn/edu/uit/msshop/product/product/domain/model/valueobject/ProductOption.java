package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductOption(
        String value) {
    public static final int MAX_LENGTH_VALUE = 20;
    public static final int MAX_RAW_LENGTH_VALUE = 30;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public ProductOption {
        if (value == null) {
            throw new DomainException("Option value CANNOT be null");
        }

        if (value.length() > MAX_RAW_LENGTH_VALUE) {
            throw new DomainException("Option value wildly exceeds acceptable technical bounds");
        }

        if (value.isBlank()) {
            throw new DomainException("Option value CANNOT be blank");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.length() > MAX_LENGTH_VALUE) {
            throw new DomainException("Option value is too long");
        }
    }
}
