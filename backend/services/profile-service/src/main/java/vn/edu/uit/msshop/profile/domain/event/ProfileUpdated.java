package vn.edu.uit.msshop.profile.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class ProfileUpdated {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();

    private final Instant occurrenceTime = Instant.now();

    private final ProfileId profileId;
}
