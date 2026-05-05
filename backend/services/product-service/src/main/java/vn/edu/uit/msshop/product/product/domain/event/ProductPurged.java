package vn.edu.uit.msshop.product.product.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class ProductPurged {
  @EqualsAndHashCode.Include
  private final UUID eventId = UUID.randomUUID();

  private final Instant occurrenceTime = Instant.now();

  private final ProductId productId;
}
