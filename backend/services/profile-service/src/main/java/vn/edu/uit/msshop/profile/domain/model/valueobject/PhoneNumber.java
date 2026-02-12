package vn.edu.uit.msshop.profile.domain.model.valueobject;

import java.util.regex.Pattern;

import org.jspecify.annotations.NullUnmarked;

public record PhoneNumber(
        String value) {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^\\+?\\d{8,15}$");

    public PhoneNumber {
        if (value == null) {
            throw new IllegalArgumentException("phone null");
        }

        value = value.trim();

        if (!PHONE_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("phone invalid");
        }
    }

    @NullUnmarked
    public static PhoneNumber fromValueOrNull(
            final String value) {
        if (value == null) {
            return null;
        }

        return new PhoneNumber(value);
    }
}
