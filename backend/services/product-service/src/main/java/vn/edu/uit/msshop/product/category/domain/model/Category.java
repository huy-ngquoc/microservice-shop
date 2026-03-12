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

    @Nullable
    private final CategoryImage image;

    public Category(
            CategoryId id,

            CategoryName name,

            @Nullable
            CategoryImage image) {
        this.id = Objects.requireNonNull(id, "Id must NOT be null");
        this.name = Objects.requireNonNull(name, "Name must NOT be null");
        this.image = image;
    }
}
