package vn.edu.uit.msshop.product.brand.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.shared.domain.exception.Domains;

public record BrandName(
        String value) {
    public static final int MAX_LENGTH = 40;
    public static final int MAX_RAW_LENGTH = (int) (MAX_LENGTH * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    // TODO: place it to somewhere that all can access
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public BrandName {
        if (value == null) {
            throw new DomainException("Brand name is null");
        }

        if (value.length() > MAX_RAW_LENGTH) {
            throw new DomainException("Brand name wildly exceeds acceptable technical bounds");
        }

        if (value.isBlank()) {
            throw new DomainException("Brand name is blank");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Brand name is too long");
        }
    }
}
