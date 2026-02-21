package vn.edu.uit.msshop.profile.application.dto.command;

import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public record ReplaceAvatarCommand(
        ProfileId profileId,
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
