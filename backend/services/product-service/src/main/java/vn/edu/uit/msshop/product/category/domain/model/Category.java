package vn.edu.uit.msshop.product.category.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.Domains;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Category {
    @EqualsAndHashCode.Include
    private final CategoryId id;

    private final CategoryName name;

    @Nullable
    private final CategoryImageKey imageKey;

    // ===== Metadata =====

    private final CategoryVersion version;

    public Category(
            final CategoryId id,

            final CategoryName name,

            @Nullable
            final CategoryImageKey imageKey,

            final CategoryVersion version) {
        this.id = Domains.requireNonNull(id, "Id must NOT be null");
        this.name = Domains.requireNonNull(name, "Name must NOT be null");
        this.imageKey = imageKey;

        this.version = Domains.requireNonNull(version, "Version must NOT be null");
    }
}
