package vn.edu.uit.msshop.product.category.domain.model;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Category {
    @EqualsAndHashCode.Include
    private final CategoryId id;

    private final CategoryName name;

    private final CategoryImageKey imageKey;

    public Category(
            CategoryId id,
            CategoryName name,
            CategoryImageKey imageKey) {
        this.id = Objects.requireNonNull(id, "Id must NOT be null");
        this.name = Objects.requireNonNull(name, "Name must NOT be null");
        this.imageKey = Objects.requireNonNull(imageKey, "Image key must NOT be null");
    }
}
