package vn.edu.uit.msshop.product.brand.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BrandCreated {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();

    private final Instant occurrenceTime = Instant.now();

    private final BrandId brandId;
}
