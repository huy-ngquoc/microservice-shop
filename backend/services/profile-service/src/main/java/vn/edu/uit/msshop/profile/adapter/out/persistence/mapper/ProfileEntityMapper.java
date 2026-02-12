package vn.edu.uit.msshop.profile.adapter.out.persistence.mapper;

import org.jspecify.annotations.NullUnmarked;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.profile.adapter.out.persistence.ProfileJpaEntity;
import vn.edu.uit.msshop.profile.domain.model.Profile;
import vn.edu.uit.msshop.profile.domain.model.valueobject.Avatar;
import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarPublicId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarUrl;
import vn.edu.uit.msshop.profile.domain.model.valueobject.EmailAddress;
import vn.edu.uit.msshop.profile.domain.model.valueobject.FullName;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ImageSize;
import vn.edu.uit.msshop.profile.domain.model.valueobject.PhoneNumber;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ShippingAddress;

@Component
public class ProfileEntityMapper {
    public Profile toDomain(
            final ProfileJpaEntity e) {
        final var avatar = ProfileEntityMapper.toAvatarOrNull(
                e.getAvatarUrl(),
                e.getAvatarPublicId(),
                e.getAvatarWidth(),
                e.getAvatarHeight());

        final var snapshot = Profile.Snapshot.builder()
                .id(new ProfileId(e.getId()))
                .fullName(new FullName(e.getFullName()))
                .address(new ShippingAddress(e.getAddress()))
                .phoneNumber(new PhoneNumber(e.getPhoneNumber()))
                .email(new EmailAddress(e.getEmail()))
                .avatar(avatar)
                .build();

        return Profile.reconstitute(snapshot);
    }

    public ProfileJpaEntity toEntity(
            final Profile p) {
        final var snapshot = p.snapshot();

        return ProfileJpaEntity.of(
                snapshot.id().value(),
                snapshot.fullName().value(),
                snapshot.email().value(),
                snapshot.phoneNumber().value(),
                snapshot.address().value(),
                snapshot.avatar().url().value(),
                snapshot.avatar().publicId().value(),
                snapshot.avatar().size().width(),
                snapshot.avatar().size().height());
    }

    @NullUnmarked
    private static Avatar toAvatarOrNull(
            String url,
            String publicId,
            Integer width,
            Integer height) {
        if ((url == null) || (publicId == null) || (width == null) || (height == null)) {
            return null;
        }

        return ProfileEntityMapper.toAvatar(url, publicId, width, height);
    }

    private static Avatar toAvatar(
            String url,
            String publicId,
            Integer width,
            Integer height) {
        final var imageSize = new ImageSize(width, height);
        return new Avatar(
                new AvatarUrl(url),
                new AvatarPublicId(publicId),
                imageSize);
    }
}
