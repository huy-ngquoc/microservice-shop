package vn.edu.uit.msshop.profile.application.port.out;

import vn.edu.uit.msshop.profile.domain.model.valueobject.Avatar;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public interface UploadAvatarPort {
    Avatar upload(
            final ProfileId profileId,
            final byte[] bytes,
            final String originalFilename,
            final String contentType);
}
