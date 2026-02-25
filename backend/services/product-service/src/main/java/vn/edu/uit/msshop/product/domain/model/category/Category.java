package vn.edu.uit.msshop.product.domain.model.category;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.domain.model.category.command.CategoryDraft;
import vn.edu.uit.msshop.product.domain.model.category.command.CategoryUpdate;
import vn.edu.uit.msshop.product.domain.model.category.snapshot.CategorySnapshot;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@Builder(
        access = AccessLevel.PRIVATE)
public final class Category {
    @NonNull
    @EqualsAndHashCode.Include
    private final CategoryId id;

    @NonNull
    private final CategoryName name;

    private final CategoryImage image;

    @NullMarked
    public static Category create(
            final CategoryDraft d) {
        if (d == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        return Category.builder()
                .id(CategoryId.newId())
                .name(d.name())
                .image(d.image())
                .build();
    }

    @NullMarked
    public static Category reconstitute(
            final CategorySnapshot s) {
        if (s == null) {
            throw new IllegalArgumentException("Snapshot must NOT be null");
        }

        return Category.builder()
                .id(s.id())
                .name(s.name())
                .image(s.image())
                .build();
    }

    @NullMarked
    public Category applyUpdate(
            final CategoryUpdate u) {
        if (u == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        return Category.builder()
                .id(this.id)
                .name(u.name())
                .image(u.image())
                .build();
    }
}
