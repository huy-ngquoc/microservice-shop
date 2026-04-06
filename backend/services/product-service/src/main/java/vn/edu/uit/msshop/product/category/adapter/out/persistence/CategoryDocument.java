package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Document("Categories")
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class CategoryDocument {
    @Id
    @EqualsAndHashCode.Include
    private final UUID id;

    private final String name;

    @Nullable
    private final String imageKey;

    @Version
    @Nullable
    private final Long version;

    @Nullable
    private final Instant deletionTime;

    @PersistenceCreator
    public CategoryDocument(
            final UUID id,

            final String name,

            @Nullable
            final String imageKey,

            @Nullable
            final Long version,

            @Nullable
            final Instant deletionTime) {
        this.id = id;
        this.name = name;
        this.imageKey = imageKey;
        this.version = version;
        this.deletionTime = deletionTime;
    }
}
