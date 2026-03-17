package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Document("Brands")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class BrandDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final String name;

    @Nullable
    private final String logoKey;

    @Version
    @Nullable
    private final Long version;

    @PersistenceCreator
    public BrandDocument(
            final UUID id,

            final String name,

            @Nullable
            final String logoKey,

            @Nullable
            final Long version) {
        this.id = id;
        this.name = name;
        this.logoKey = logoKey;
        this.version = version;
    }
}
