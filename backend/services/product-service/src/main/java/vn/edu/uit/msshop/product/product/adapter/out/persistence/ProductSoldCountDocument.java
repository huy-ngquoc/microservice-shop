package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("ProductSoldCounts")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class ProductSoldCountDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final int soldCount;

    private final Instant lastUpdatedTime;

    public ProductSoldCountDocument(
            final UUID id,
            final int soldCount) {
        this.id = id;
        this.soldCount = soldCount;
        this.lastUpdatedTime = Instant.now();
    }

    @PersistenceCreator
    ProductSoldCountDocument(
            final UUID id,
            final int soldCount,
            final Instant lastUpdatedTime) {
        this.id = id;
        this.soldCount = soldCount;
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
