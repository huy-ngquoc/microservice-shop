package vn.edu.uit.msshop.profile.domain.model.valueobject;

import java.util.regex.Pattern;

public record FullName(
        String value) {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    public FullName {
        if (value == null) {
            throw new IllegalArgumentException("fullName null");
        }

        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new IllegalArgumentException("fullName blank");
        }

        if (value.length() > 80) {
            throw new IllegalArgumentException("fullName too long");
        }
    }
}
