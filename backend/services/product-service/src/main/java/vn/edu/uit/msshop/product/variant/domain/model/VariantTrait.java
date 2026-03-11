package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantTrait(
        String name,
        String value) {
    public static final int MAX_LENGTH_NAME = 20;
    public static final int MAX_RAW_LENGTH_NAME = 30;
    public static final int MAX_LENGTH_VALUE = 20;
    public static final int MAX_RAW_LENGTH_VALUE = 30;

    private static final VariantTrait DEFAULT_TRAIT = new VariantTrait("Standard", "Default");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public VariantTrait {
        name = VariantTrait.validateAndNormalizeName(name);
        value = VariantTrait.validateAndNormalizeValue(value);
    }

    private static String validateAndNormalizeName(
            final String name) {
        if (name == null) {
            throw new DomainException("Option name CANNOT be null");
        }

        if (name.length() > MAX_RAW_LENGTH_NAME) {
            throw new DomainException("Option name wildly exceeds acceptable technical bounds");
        }

        if (name.isBlank()) {
            throw new DomainException("Option name CANNOT be blank");
        }

        final var normalizedName = WHITESPACE_PATTERN.matcher(name.trim()).replaceAll(" ");

        if (normalizedName.length() > MAX_LENGTH_NAME) {
            throw new DomainException("Option name is too long");
        }

        return normalizedName;
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

}
