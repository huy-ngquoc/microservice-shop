package vn.edu.uit.msshop.profile.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.adapter.out.persistence.mapper.ProfileEntityMapper;
import vn.edu.uit.msshop.profile.application.port.out.LoadProfilePort;
import vn.edu.uit.msshop.profile.application.port.out.SaveProfilePort;
import vn.edu.uit.msshop.profile.domain.model.Profile;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter
        implements LoadProfilePort, SaveProfilePort {
    private final SpringDataProfileJpaRepository repository;
    private final ProfileEntityMapper mapper;

    @Override
    public Optional<Profile> loadById(
            ProfileId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

    @Override
    public Profile save(
            final Profile profile) {
        final var toSave = this.mapper.toEntity(profile);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

}
