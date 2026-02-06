package vn.edu.uit.msshop.profile.domain.model.valueobject;

import org.jspecify.annotations.NonNull;

public record AvatarUrl(
        @NonNull
        String value) {
    public AvatarUrl {
        if (value == null) {
            throw new IllegalArgumentException("avatarUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new IllegalArgumentException("avatarUrl invalid");
        }
    }
}
