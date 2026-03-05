package vn.edu.uit.msshop.product.product.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductOption(
        String name,
        List<String> values) {
    public static final int MAX_LENGTH_NAME = 20;
    public static final int MAX_RAW_LENGTH_NAME = 30;
    public static final int MAX_LENGTH_VALUE = 20;
    public static final int MAX_RAW_LENGTH_VALUE = 30;
    public static final int MAX_AMOUNT_VALUES = 10;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public ProductOption {
        name = ProductOption.validateAndNormalizeName(name);
        values = ProductOption.validateAndNormalizeValues(values);
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

    private static List<String> validateAndNormalizeValues(
            final List<String> values) {
        if (values == null) {
            throw new DomainException("Option values CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT_VALUES) {
            throw new DomainException("Option values is too many");
        }

        final var processedValues = new ArrayList<String>(values.size());
        final var setValues = HashSet.<String>newHashSet(values.size());

        for (final var rawValue : values) {
            final var normalizedValue = ProductOption.validateAndNormalizeValue(rawValue);

            final var lowercaseValue = normalizedValue.toLowerCase(Locale.ROOT);
            if (!setValues.add(lowercaseValue)) {
                throw new DomainException("Duplicate option value found: " + normalizedValue);
            }

            processedValues.add(normalizedValue);
        }

        return List.copyOf(processedValues);
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
