package vn.edu.uit.msshop.profile.application.port.out;

import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarPublicId;

public interface DeleteAvatarPort {
    void deleteByPublicId(
            final AvatarPublicId publicId);
}
