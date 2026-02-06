package vn.edu.uit.msshop.profile.domain.model.valueobject;

import org.jspecify.annotations.NonNull;

public record AvatarPublicId(
        @NonNull
        String value) {
    public AvatarPublicId {
        if (value == null) {
            throw new IllegalArgumentException("publicId null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException("publicId blank");
        }
    }
}
