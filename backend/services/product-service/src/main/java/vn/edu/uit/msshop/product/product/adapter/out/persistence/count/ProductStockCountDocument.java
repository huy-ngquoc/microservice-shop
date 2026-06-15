package vn.edu.uit.msshop.product.product.adapter.out.persistence.count;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("ProductStockCounts")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
final class ProductStockCountDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID productId;

    private final int value;

    private final Instant lastUpdatedTime;

    @PersistenceCreator
    public ProductStockCountDocument(
            final UUID productId,
            final int value,
            final Instant lastUpdatedTime) {
        this.productId = productId;
        this.value = value;
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
