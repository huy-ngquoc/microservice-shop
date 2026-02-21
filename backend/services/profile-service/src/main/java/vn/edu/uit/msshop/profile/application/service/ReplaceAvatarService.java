package vn.edu.uit.msshop.profile.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.dto.command.ReplaceAvatarCommand;
import vn.edu.uit.msshop.profile.application.dto.query.AvatarView;
import vn.edu.uit.msshop.profile.application.exception.ProfileNotFoundException;
import vn.edu.uit.msshop.profile.application.port.in.ReplaceAvatarUseCase;
import vn.edu.uit.msshop.profile.application.port.out.DeleteAvatarPort;
import vn.edu.uit.msshop.profile.application.port.out.LoadProfilePort;
import vn.edu.uit.msshop.profile.application.port.out.PublishProfileEventPort;
import vn.edu.uit.msshop.profile.application.port.out.SaveProfilePort;
import vn.edu.uit.msshop.profile.application.port.out.UploadAvatarPort;
import vn.edu.uit.msshop.profile.domain.event.ProfileUpdated;

@Service
@RequiredArgsConstructor
public class ReplaceAvatarService implements ReplaceAvatarUseCase {
    private final LoadProfilePort loadProfilePort;
    private final SaveProfilePort saveProfilePort;
    private final UploadAvatarPort uploadAvatarPort;
    private final DeleteAvatarPort deleteAvatarPort;
    private final PublishProfileEventPort eventPort;

    @Override
    @Transactional
    public AvatarView replace(
            final ReplaceAvatarCommand command) {
        final var oldProfile = this.loadProfilePort.loadById(command.profileId())
                .orElseThrow(() -> new ProfileNotFoundException(command.profileId()));

        final var uploadedAvatar = this.uploadAvatarPort.upload(
                command.profileId(),
                command.bytes(),
                command.originalFilename(),
                command.contentType());

        final var newProfile = oldProfile.withAvatar(uploadedAvatar);

        final var avatarView = new AvatarView(
                uploadedAvatar.url().value(),
                uploadedAvatar.publicId().value(),
                uploadedAvatar.size().width(),
                uploadedAvatar.size().height());

        if (newProfile == oldProfile) {
            return avatarView;
        }

        final var oldAvatar = oldProfile.getAvatar();
        if ((oldAvatar != null) && (!oldAvatar.publicId().equals(uploadedAvatar.publicId()))) {
            this.deleteAvatarPort.deleteByPublicId(oldAvatar.publicId());
        }

        this.saveProfilePort.save(newProfile);
        this.eventPort.publish(new ProfileUpdated(newProfile.getId()));

        return avatarView;
    }
}
