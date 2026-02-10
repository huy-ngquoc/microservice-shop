package vn.edu.uit.msshop.profile.application.port.out;

import vn.edu.uit.msshop.profile.domain.model.Profile;

public interface SaveProfilePort {
    Profile save(
            final Profile profile);
}
