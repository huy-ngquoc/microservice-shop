package vn.edu.uit.msshop.profile.application.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.dto.command.UpdateProfileCommand;
import vn.edu.uit.msshop.profile.application.exception.ProfileNotFoundException;
import vn.edu.uit.msshop.profile.application.port.in.UpdateProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.out.LoadProfilePort;
import vn.edu.uit.msshop.profile.application.port.out.PublishProfileEventPort;
import vn.edu.uit.msshop.profile.application.port.out.SaveProfilePort;
import vn.edu.uit.msshop.profile.domain.event.ProfileUpdated;
import vn.edu.uit.msshop.profile.domain.model.Profile;

@Service
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileUseCase {
    private final LoadProfilePort loadPort;
    private final SaveProfilePort savePort;
    private final PublishProfileEventPort eventPort;

    @Override
    @Transactional
    public void update(
            @NonNull
            final UpdateProfileCommand command) {
        final var profile = this.loadPort.loadById(command.profileId())
                .orElseThrow(() -> new ProfileNotFoundException(command.profileId()));

        final var update = Profile.UpdateInfo.builder()
                .id(command.profileId())
                .fullName(command.fullName().apply(profile.getFullName()))
                .email(command.email().apply(profile.getEmail()))
                .phoneNumber(command.phoneNumber().apply(profile.getPhoneNumber()))
                .build();
        final var next = profile.applyUpdateInfo(update);

        if (next == profile) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new ProfileUpdated(saved.getId()));
    }
}
