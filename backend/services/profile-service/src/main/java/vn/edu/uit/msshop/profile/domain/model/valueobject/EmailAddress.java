package vn.edu.uit.msshop.profile.domain.model.valueobject;

import java.util.Locale;
import java.util.regex.Pattern;

import org.jspecify.annotations.NullUnmarked;

public record EmailAddress(
        String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[^@\\p{Z}\\t\\n\\r\\f]+@[^@\\p{Z}\\t\\n\\r\\f]+\\.[^@\\p{Z}\\t\\n\\r\\f]+$");

    public EmailAddress {
        if (value == null) {
            throw new IllegalArgumentException("email null");
        }

        value = value.trim().toLowerCase(Locale.ROOT);

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("email invalid");
        }
    }

    @NullUnmarked
    public static EmailAddress fromValueOrNull(
            final String value) {
        if (value == null) {
            return null;
        }

        return new EmailAddress(value);
    }
}
