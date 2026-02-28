package vn.edu.uit.msshop.product.domain.model.category;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.domain.model.category.command.CategoryDraft;
import vn.edu.uit.msshop.product.domain.model.category.command.CategoryUpdateInfo;
import vn.edu.uit.msshop.product.domain.model.category.snapshot.CategorySnapshot;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Category {
    @EqualsAndHashCode.Include
    private final CategoryId id;

    private final CategoryName name;

    @Nullable
    private final CategoryImage image;

    private Category(
            CategoryId id,

            CategoryName name,

            @Nullable
            CategoryImage image) {
        this.id = Objects.requireNonNull(id, "Id must NOT be null");
        this.name = Objects.requireNonNull(name, "Name must NOT be null");
        this.image = image;
    }

    public static Category create(
            final CategoryDraft draft) {
        if (draft == null) {
            throw new IllegalArgumentException("Draft must NOT be null");
        }

        return new Category(
                CategoryId.newId(),
                draft.name(),
                null);
    }

    public static Category reconstitute(
            final CategorySnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot must NOT be null");
        }

        return new Category(
                snapshot.id(),
                snapshot.name(),
                snapshot.image());
    }

    public Category applyUpdateInfo(
            final CategoryUpdateInfo updateInfo) {
        if (updateInfo == null) {
            throw new IllegalArgumentException("Update must NOT be null");
        }

        if (this.isSameInfoAs(updateInfo)) {
            return this;
        }

        return new Category(
                this.id,
                updateInfo.name(),
                this.image);
    }

    public Category withImage(
            final CategoryImage newImage) {
        if (Objects.equals(this.image, newImage)) {
            return this;
        }

        return new Category(
                this.id,
                this.name,
                newImage);
    }

    public Category withoutImage() {
        if (this.image == null) {
            return this;
        }

        return new Category(
                this.id,
                this.name,
                null);
    }

    public CategorySnapshot snapshot() {
        return CategorySnapshot.builder()
                .id(id)
                .name(this.name)
                .image(this.image)
                .build();
    }

    private boolean isSameInfoAs(
            final CategoryUpdateInfo updateInfo) {
        return Objects.equals(updateInfo.name(), this.name);
    }
}
