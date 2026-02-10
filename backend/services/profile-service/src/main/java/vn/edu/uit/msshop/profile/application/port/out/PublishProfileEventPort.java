package vn.edu.uit.msshop.profile.application.port.out;

import vn.edu.uit.msshop.profile.domain.event.ProfileCreated;
import vn.edu.uit.msshop.profile.domain.event.ProfileUpdated;

public interface PublishProfileEventPort {
    void publish(
            final ProfileCreated event);

    void publish(
            final ProfileUpdated event);
}
