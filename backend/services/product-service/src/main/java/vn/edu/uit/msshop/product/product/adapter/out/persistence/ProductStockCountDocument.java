package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("ProductStockCounts")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public class ProductStockCountDocument {
  @Id
  @EqualsAndHashCode.Include
  private final UUID id;

  private final int value;

  private final Instant lastUpdatedTime;

  public ProductStockCountDocument(final UUID id, final int value, final Instant lastUpdatedTime) {
    this.id = id;
    this.value = value;
    this.lastUpdatedTime = lastUpdatedTime;
  }
}
