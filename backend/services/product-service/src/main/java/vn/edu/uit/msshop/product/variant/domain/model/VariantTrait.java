package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantTrait(
        String value) {
    public static final int MAX_LENGTH_VALUE = 20;
    public static final int MAX_RAW_LENGTH_VALUE = (int) (MAX_LENGTH_VALUE * Domains.RAW_LENGTH_TOLERANCE_FACTOR);

    private static final VariantTrait DEFAULT_TRAIT = new VariantTrait("Default");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public VariantTrait {
        value = VariantTrait.validateAndNormalizeValue(value);
    }

    public static String validateAndNormalizeValue(
            final String value) {
        if (value == null) {
            throw new DomainException("Option value CANNOT be null");
        }

        if (value.length() > MAX_RAW_LENGTH_VALUE) {
            throw new DomainException("Option value wildly exceeds acceptable technical bounds");
        }

        if (value.isBlank()) {
            throw new DomainException("Option value CANNOT be blank");
        }

        final var normalizedValue = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (normalizedValue.length() > MAX_LENGTH_VALUE) {
            throw new DomainException("Option value is too long");
        }

        return normalizedValue;
    }

    public static VariantTrait getDefault() {
        return VariantTrait.DEFAULT_TRAIT;
    }
}
