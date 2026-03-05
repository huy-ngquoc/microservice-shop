package vn.uit.edu.msshop.rating.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public class RatingPosted {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();
    private final Instant occurentTime = Instant.now();
    private final RatingId ratingId;
    
}
