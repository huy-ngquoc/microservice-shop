package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.UUID;

import org.jspecify.annotations.NonNull;
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

@Document("Brands")
@Getter
@Setter
@SuppressWarnings("NullAway.Init")
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class BrandDocument {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    @Nullable
    private BrandLogoDocument logo;

    @Version
    @Nullable
    private Long version;

    public BrandDocument(
            UUID id,
            String name,
            @Nullable
            BrandLogoDocument logo) {
        this(id, name, logo, null);
    }
}
