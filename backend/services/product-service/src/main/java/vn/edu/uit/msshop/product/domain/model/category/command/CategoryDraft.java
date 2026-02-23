package vn.edu.uit.msshop.product.domain.model.category.command;

import org.jspecify.annotations.NonNull;

import lombok.Builder;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Builder
public record CategoryDraft(
        @NonNull
        CategoryName name,

        CategoryImage image) {
    public CategoryDraft {
        if (name == null) {
            throw new IllegalArgumentException("Name must NOT be null");
        }
    }
}
