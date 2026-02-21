package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.application.dto.query.AvatarView;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public interface FindAvatarUseCase {
    AvatarView findById(
            final ProfileId id);
}
