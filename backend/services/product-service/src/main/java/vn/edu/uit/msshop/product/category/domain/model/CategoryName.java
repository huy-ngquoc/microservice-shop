package vn.edu.uit.msshop.product.category.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CategoryName(
        String value) {
    public static final int MAX_LENGTH = 40;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public CategoryName {
        if (value == null) {
            throw new DomainException("Category name is null");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Category name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Category name is too long");
        }
    }
}
