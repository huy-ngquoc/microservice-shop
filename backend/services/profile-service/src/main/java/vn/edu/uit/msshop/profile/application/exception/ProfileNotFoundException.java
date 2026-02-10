package vn.edu.uit.msshop.profile.application.exception;

import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(
            final ProfileId profileId) {
        super("Profile not found with ID: " + profileId.value());
    }
}
