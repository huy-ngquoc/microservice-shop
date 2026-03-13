package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Categories")
@Getter
@Setter
@SuppressWarnings("NullAway.Init")
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class CategoryDocument {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private String imageKey;

    @Version
    @Nullable
    private Long version;

    public CategoryDocument(
            final UUID id,
            final String name,
            final String image) {
        this(
                id,
                name,
                image,
                null);
    }
}
