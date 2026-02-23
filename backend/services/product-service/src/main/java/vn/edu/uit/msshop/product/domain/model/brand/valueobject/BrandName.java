package vn.edu.uit.msshop.product.domain.model.brand.valueobject;

import java.util.regex.Pattern;

public record BrandName(
        String value) {
    // TODO: place it to somewhere that all can access
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public BrandName {
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
