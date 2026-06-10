package vn.edu.uit.msshop.product.product.adapter.out.persistence.rating;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("ProductRatings")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class ProductRatingDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final long total;

    private final int amount;

    private final Instant lastUpdatedTime;

    public ProductRatingDocument(
            final UUID id,
            final long total,
            final int amount) {
        this.id = id;
        this.total = total;
        this.amount = amount;
        this.lastUpdatedTime = Instant.now();
    }

    @PersistenceCreator
    ProductRatingDocument(
            final UUID id,
            final long total,
            final int amount,
            final Instant lastUpdatedTime) {
        this.id = id;
        this.total = total;
        this.amount = amount;
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
