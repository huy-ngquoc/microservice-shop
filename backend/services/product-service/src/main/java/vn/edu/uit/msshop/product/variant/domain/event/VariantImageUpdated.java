package vn.edu.uit.msshop.product.variant.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class VariantImageUpdated {
  @EqualsAndHashCode.Include
  private final UUID eventId = UUID.randomUUID();

  private final Instant occurrenceTime = Instant.now();

  private final VariantId categoryId;

  @Nullable
  private final VariantImageKey newImageKey;

  @Nullable
  private final VariantImageKey oldImageKey;
}
