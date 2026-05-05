package vn.edu.uit.msshop.product.category.domain.event;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoryImageUpdated {
  @EqualsAndHashCode.Include
  private final UUID eventId = UUID.randomUUID();

  private final Instant occurrenceTime = Instant.now();

  private final CategoryId categoryId;

  @Nullable
  private final CategoryImageKey newImageKey;

  @Nullable
  private final CategoryImageKey oldImageKey;
}
