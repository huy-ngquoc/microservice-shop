package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.application.dto.query.ProfileView;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public interface FindProfileUseCase {
    ProfileView findById(
            final ProfileId id);
}
