package vn.edu.uit.msshop.product.product.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductName(
        String value) {
    public static final int MAX_LENGTH = 40;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public ProductName {
        if (value == null) {
            throw new DomainException("Product name is null");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Product name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Product name is too long");
        }
    }
}
