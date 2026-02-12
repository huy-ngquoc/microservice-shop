package vn.edu.uit.msshop.profile.domain.model.valueobject;

import org.jspecify.annotations.NullUnmarked;

public record ShippingAddress(
        String value) {
    public ShippingAddress {
        if (value == null) {
            throw new IllegalArgumentException("address null");
        }

        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("address blank");
        }

        if (value.length() > 255) {
            throw new IllegalArgumentException("address too long");
        }
    }

    @NullUnmarked
    public static ShippingAddress fromValueOrNull(
            final String value) {
        if (value == null) {
            return null;
        }

        return new ShippingAddress(value);
    }
}
