package vn.edu.uit.msshop.profile.domain.model.valueobject;

import org.jspecify.annotations.NonNull;

public record Avatar(
        @NonNull
        AvatarUrl url,

        @NonNull
        AvatarPublicId publicId,

        @NonNull
        ImageSize size) {
    public Avatar {
        if ((url == null) || (publicId == null) || (size == null)) {
            throw new IllegalArgumentException("avatar fields must not be null");
        }
    }
}
