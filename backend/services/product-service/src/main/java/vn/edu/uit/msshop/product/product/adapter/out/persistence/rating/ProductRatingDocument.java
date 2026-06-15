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
final class ProductRatingDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID productId;

    private final long total;

    private final int amount;

    private final Instant lastUpdatedTime;

    @PersistenceCreator
    public ProductRatingDocument(
            final UUID productId,
            final long total,
            final int amount,
            final Instant lastUpdatedTime) {
        this.productId = productId;
        this.total = total;
        this.amount = amount;
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
