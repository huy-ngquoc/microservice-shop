package vn.edu.uit.msshop.profile.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.dto.command.CreateProfileCommand;
import vn.edu.uit.msshop.profile.application.port.in.CreateProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.out.PublishProfileEventPort;
import vn.edu.uit.msshop.profile.application.port.out.SaveProfilePort;
import vn.edu.uit.msshop.profile.domain.event.ProfileCreated;
import vn.edu.uit.msshop.profile.domain.model.Profile;

@Service
@RequiredArgsConstructor
public class CreateProfileService implements CreateProfileUseCase {
    private final SaveProfilePort savePort;
    private final PublishProfileEventPort eventPort;

    @Override
    @Transactional
    public void create(
            final CreateProfileCommand cmd) {
        final var draft = Profile.Draft.builder()
                .id(cmd.profileId())
                .fullName(cmd.fullName())
                .email(cmd.email())
                .build();
        final var profile = Profile.create(draft);
        final var saved = this.savePort.save(profile);

        this.eventPort.publish(new ProfileCreated(saved.getId()));
    }
}
