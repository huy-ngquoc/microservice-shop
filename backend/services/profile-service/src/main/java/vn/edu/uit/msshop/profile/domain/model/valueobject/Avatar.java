package vn.edu.uit.msshop.profile.domain.model.valueobject;

public record Avatar(
        AvatarUrl url,
        AvatarPublicId publicId,
        ImageSize size) {
    public Avatar {
        if ((url == null) || (publicId == null) || (size == null)) {
            throw new IllegalArgumentException("avatar fields must not be null");
        }
    }
}
