package vn.edu.uit.msshop.product.brand.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandName(
        String value) {
    public static final int MAX_LENGTH = 40;

    // TODO: place it to somewhere that all can access
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public BrandName {
        if (value == null) {
            throw new DomainException("Brand name is null");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new DomainException("Brand name is blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Brand name is too long");
        }
    }
}
