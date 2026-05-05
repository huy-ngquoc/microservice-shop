package vn.edu.uit.msshop.shared.domain;

import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public final class Domains {
    public static final double RAW_LENGTH_TOLERANCE_FACTOR = 1.25;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");

    private Domains() {
    }

    public static Pattern getWhitespacePattern() {
        return Domains.WHITESPACE_PATTERN;
    }

    public static <T> T requireNonNull(
            @Nullable
            final T obj,

            final String msg) {
        if (obj == null) {
            throw new DomainException(msg);
        }

        return obj;
    }

    public static String requireNonBlank(
            @Nullable
            final String str,

            final String msg) {
        if ((str == null) || str.isBlank()) {
            throw new DomainException(msg);
        }

        return str;
    }
}
