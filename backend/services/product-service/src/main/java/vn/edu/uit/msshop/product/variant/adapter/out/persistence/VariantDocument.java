package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Document("Variants")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class VariantDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final UUID productId;

    private final int price;

    private final int sold;

    private final List<String> traits;

    @Nullable
    private final String imageKey;

    @Version
    @Nullable
    private final Long version;

    @PersistenceCreator
    public VariantDocument(
            final UUID id,

            final UUID productId,

            final int price,

            final int sold,

            final List<String> traits,

            @Nullable
            final String imageKey,

            @Nullable
            final Long version) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.sold = sold;
        this.traits = List.copyOf(traits);
        this.imageKey = imageKey;
        this.version = version;
    }
}
