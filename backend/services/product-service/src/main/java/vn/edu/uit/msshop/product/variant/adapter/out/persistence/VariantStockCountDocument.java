package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("VariantStockCounts")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class VariantStockCountDocument {
  @Id
  @EqualsAndHashCode.Include
  private final UUID variantId;

  private final UUID productId;

  private final int value;

  private final Instant lastUpdatedTime;

  @PersistenceCreator
  VariantStockCountDocument(final UUID variantId, final UUID productId, int value,
      Instant lastUpdatedTime) {
    this.variantId = variantId;
    this.productId = productId;
    this.value = value;
    this.lastUpdatedTime = lastUpdatedTime;
  }
}
