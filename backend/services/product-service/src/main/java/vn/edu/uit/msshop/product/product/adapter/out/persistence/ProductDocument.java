package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Document("Products")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
public class ProductDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final String name;

    private final UUID categoryId;

    private final UUID brandId;

    private final long minPrice;

    private final long maxPrice;

    private final int soldCount;

    private final float ratingAverage;

    private final int ratingCount;

    private final List<String> options;

    private final List<ProductVariantDocument> variants;

    private final List<String> imageKeys;

    @Version
    @Nullable
    private final Long version;

    @Nullable
    private final Instant deletionTime;

    @PersistenceCreator
    public ProductDocument(
            final UUID id,
            final String name,
            final UUID categoryId,
            final UUID brandId,
            final long minPrice,
            final long maxPrice,
            final int soldCount,
            final float ratingAverage,
            final int ratingCount,
            final List<String> options,
            final List<ProductVariantDocument> variants,
            final List<String> imageKeys,
            @Nullable
            final Long version,
            @Nullable
            final Instant deletionTime) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.soldCount = soldCount;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;
        this.options = List.copyOf(options);
        this.variants = List.copyOf(variants);
        this.imageKeys = List.copyOf(imageKeys);
        this.version = version;
        this.deletionTime = deletionTime;
    }
}
