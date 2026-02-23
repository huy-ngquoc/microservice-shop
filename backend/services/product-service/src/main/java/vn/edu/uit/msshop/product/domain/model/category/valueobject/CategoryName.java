package vn.edu.uit.msshop.product.domain.model.category.valueobject;

import java.util.regex.Pattern;

public record CategoryName(
        String value) {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public CategoryName {
        if (value == null) {
            throw new IllegalArgumentException("Category name is null");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new IllegalArgumentException("Category name is blank");
        }

        if (value.length() > 40) {
            throw new IllegalArgumentException("Category name is too long");
        }
    }
}
