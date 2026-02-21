package vn.edu.uit.msshop.profile.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.profile.application.dto.query.AvatarView;
import vn.edu.uit.msshop.profile.application.dto.query.ProfileView;
import vn.edu.uit.msshop.profile.domain.model.Profile;
import vn.edu.uit.msshop.profile.domain.model.valueobject.Avatar;

@Component
public class ProfileViewMapper {
    public ProfileView toView(
            final Profile profile) {
        return new ProfileView(
                profile.getId().toString(),
                profile.getFullName().value(),
                profile.getEmail().value(),
                profile.getPhoneNumber().value(),
                profile.getAddress().value(),
                profile.getAvatar().url().value());
    }

    public AvatarView toView(
            final Avatar avatar) {
        return new AvatarView(
                avatar.url().value(),
                avatar.publicId().value(),
                avatar.size().width(),
                avatar.size().height());
    }
}
