package vn.edu.uit.msshop.product.brand.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class BrandLogoUpdated {
  @EqualsAndHashCode.Include
  private final UUID eventId = UUID.randomUUID();

  private final Instant occurrenceTime = Instant.now();

  private final BrandId brandId;

  @Nullable
  private final BrandLogoKey newLogoKey;

  @Nullable
  private final BrandLogoKey oldLogoKey;
}
