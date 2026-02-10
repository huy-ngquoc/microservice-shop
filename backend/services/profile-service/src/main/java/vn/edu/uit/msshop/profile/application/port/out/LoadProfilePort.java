package vn.edu.uit.msshop.profile.application.port.out;

import java.util.Optional;

import vn.edu.uit.msshop.profile.domain.model.Profile;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

public interface LoadProfilePort {
    Optional<Profile> loadById(
            final ProfileId id);
}
