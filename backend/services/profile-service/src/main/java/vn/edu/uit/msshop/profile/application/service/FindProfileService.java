package vn.edu.uit.msshop.profile.application.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.dto.query.ProfileView;
import vn.edu.uit.msshop.profile.application.exception.ProfileNotFoundException;
import vn.edu.uit.msshop.profile.application.mapper.ProfileViewMapper;
import vn.edu.uit.msshop.profile.application.port.in.FindProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.out.LoadProfilePort;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@Service
@RequiredArgsConstructor
public class FindProfileService implements FindProfileUseCase {
    private final LoadProfilePort loadPort;
    private final ProfileViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public @NonNull ProfileView findById(
            @NonNull
            final ProfileId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

}
