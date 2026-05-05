package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("VariantSoldCounts")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class VariantSoldCountDocument {
  @Id
  @EqualsAndHashCode.Include
  private final UUID variantId;

  private final UUID productId;

  private final int value;

  private final Instant lastUpdatedTime;

  public VariantSoldCountDocument(final UUID variantId, final UUID productId, final int value) {
    this.variantId = variantId;
    this.productId = productId;
    this.value = value;
    this.lastUpdatedTime = Instant.now();
  }

  @PersistenceCreator
  VariantSoldCountDocument(final UUID variantId, final UUID productId, final int value,
      final Instant lastUpdatedTime) {
    this.variantId = variantId;
    this.productId = productId;
    this.value = value;
    this.lastUpdatedTime = lastUpdatedTime;
  }
}
