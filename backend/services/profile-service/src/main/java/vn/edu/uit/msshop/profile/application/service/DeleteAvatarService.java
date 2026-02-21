package vn.edu.uit.msshop.profile.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.port.in.DeleteAvatarUseCase;
import vn.edu.uit.msshop.profile.application.port.out.DeleteAvatarPort;
import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarPublicId;

@Service
@RequiredArgsConstructor
public class DeleteAvatarService implements DeleteAvatarUseCase {
    private final DeleteAvatarPort deletePort;

    @Override
    public void deleteByPublicId(
            final AvatarPublicId publicId) {
        this.deletePort.deleteByPublicId(publicId);
    }

}
