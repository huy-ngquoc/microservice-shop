package vn.edu.uit.msshop.product.category.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.shared.domain.exception.Domains;

public record CategoryName(
        String value) {
    public static final int MAX_LENGTH = 40;
    public static final int MAX_RAW_LENGTH = (int) (MAX_LENGTH * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

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

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Category name is too long");
        }
    }
}
