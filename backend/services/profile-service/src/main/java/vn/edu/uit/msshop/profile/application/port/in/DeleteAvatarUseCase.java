package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarPublicId;

public interface DeleteAvatarUseCase {
    void deleteByPublicId(
            final AvatarPublicId publicId);
}
