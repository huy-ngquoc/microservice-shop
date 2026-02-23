package vn.edu.uit.msshop.product.domain.model.category.snapshot;

import org.jspecify.annotations.NonNull;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

public record CategorySnapshot(
        @NonNull
        CategoryId id,

        @NonNull
        CategoryName name,

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