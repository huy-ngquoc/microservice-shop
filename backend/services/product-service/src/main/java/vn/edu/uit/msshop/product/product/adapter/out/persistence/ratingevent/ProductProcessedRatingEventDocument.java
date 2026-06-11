package vn.edu.uit.msshop.product.product.adapter.out.persistence.ratingevent;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Document("ProductProcessedRatingEvents")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
final class ProductProcessedRatingEventDocument {

    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    @Indexed(
            expireAfter = "30d")
    private final Instant processedTime;

    @PersistenceCreator
    public ProductProcessedRatingEventDocument(
            final UUID id,
            final Instant processedTime) {
        this.id = id;
        this.processedTime = processedTime;
    }

    public ProductProcessedRatingEventDocument(
            final UUID id) {
        this(id, Instant.now());
    }
}
