package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
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

    @Version
    @Nullable
    private final Long version;

    private final Instant lastUpdatedTime;

    public ProductSoldCountDocument(
            final UUID id,
            final int soldCount,
            @Nullable
            final Long version) {
        this.id = id;
        this.soldCount = soldCount;
        this.version = version;
        this.lastUpdatedTime = Instant.now();
    }

    @PersistenceCreator
    ProductSoldCountDocument(
            final UUID id,
            final int soldCount,
            final long version,
            final Instant lastUpdatedTime) {
        this.id = id;
        this.soldCount = soldCount;
        this.version = version;
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
