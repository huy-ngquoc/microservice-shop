package vn.edu.uit.msshop.profile.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.dto.query.AvatarView;
import vn.edu.uit.msshop.profile.application.exception.ProfileNotFoundException;
import vn.edu.uit.msshop.profile.application.mapper.ProfileViewMapper;
import vn.edu.uit.msshop.profile.application.port.in.FindAvatarUseCase;
import vn.edu.uit.msshop.profile.application.port.out.LoadProfilePort;
import vn.edu.uit.msshop.profile.domain.model.Profile;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@Service
@RequiredArgsConstructor
public class FindAvatarService implements FindAvatarUseCase {
    private final LoadProfilePort loadPort;
    private final ProfileViewMapper mapper;

    public AvatarView findById(
            final ProfileId id) {
        return this.loadPort.loadById(id)
                .map(Profile::getAvatar)
                .map(this.mapper::toView)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }
}
