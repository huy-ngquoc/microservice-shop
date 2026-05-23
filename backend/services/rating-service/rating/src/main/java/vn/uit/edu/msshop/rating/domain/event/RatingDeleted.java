package vn.uit.edu.msshop.rating.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class RatingDeleted {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final RatingId ratingId;

    private final ProductId productId;

    private final RatingPoint point;
}
