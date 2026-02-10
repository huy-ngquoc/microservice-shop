package vn.edu.uit.msshop.profile.application.mapper;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.profile.application.dto.query.ProfileView;
import vn.edu.uit.msshop.profile.domain.model.Profile;

@Component
public class ProfileViewMapper {
    public @NonNull ProfileView toView(
            @NonNull
            final Profile profile) {
        return new ProfileView(
                profile.getId().toString(),
                profile.getFullName().value(),
                profile.getEmail().value(),
                profile.getPhoneNumber().value(),
                profile.getAddress().value(),
                profile.getAvatar().url().value());
    }
}
