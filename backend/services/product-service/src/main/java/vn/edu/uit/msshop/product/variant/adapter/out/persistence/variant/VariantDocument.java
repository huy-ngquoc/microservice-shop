package vn.edu.uit.msshop.product.variant.adapter.out.persistence.variant;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("Variants")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class VariantDocument {

    private static final String TRAITS_KEY_DELIMITER = "|";

    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final UUID productId;

    private final String productName;

    private final long price;

    private final List<String> traits;

    private final String traitsKey;

    private final List<String> targets;

    @Nullable
    private final String imageKey;

    @Version
    @Nullable
    private final Long version;

    @Nullable
    private final Instant deletionTime;

    @PersistenceCreator
    public VariantDocument(
            final UUID id,

            final UUID productId,

            final String productName,

            final long price,

            final List<String> traits,

            final List<String> targets,

            @Nullable
            final String imageKey,

            @Nullable
            final Long version,

            @Nullable
            final Instant deletionTime) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.traits = List.copyOf(traits);
        this.traitsKey = VariantDocument.computeTraitsKey(traits);
        this.targets = List.copyOf(targets);
        this.imageKey = imageKey;
        this.version = version;
        this.deletionTime = deletionTime;
    }

    private static String computeTraitsKey(
            final List<String> traits) {
        return traits.stream()
                .map(trait -> trait.toLowerCase(Locale.ROOT))
                .map(trait -> trait.length() + ":" + trait)
                .collect(Collectors.joining(TRAITS_KEY_DELIMITER));
    }
}
