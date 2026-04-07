package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class VariantRestored {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();

    private final Instant occurrenceTime = Instant.now();

    private final VariantId variantId;
}