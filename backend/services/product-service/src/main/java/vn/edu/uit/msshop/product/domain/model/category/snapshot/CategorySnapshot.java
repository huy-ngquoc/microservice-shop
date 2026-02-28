package vn.edu.uit.msshop.product.domain.model.category.snapshot;

import org.jspecify.annotations.Nullable;

import lombok.Builder;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Builder
public record CategorySnapshot(
        CategoryId id,

        CategoryName name,

        @Nullable
        CategoryImage image) {
    public CategorySnapshot {
        if (id == null) {
            throw new IllegalArgumentException("Id must NOT be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name must NOT be null");
        }
    }
}