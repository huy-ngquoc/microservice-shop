package vn.edu.uit.msshop.product.category.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class CategoryPurged {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();

    private final Instant occurrenceTime = Instant.now();

    private final CategoryId categoryId;
}
