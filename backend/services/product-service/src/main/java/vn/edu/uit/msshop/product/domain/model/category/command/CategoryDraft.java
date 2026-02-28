package vn.edu.uit.msshop.product.domain.model.category.command;

import lombok.Builder;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Builder
public record CategoryDraft(
        CategoryName name) {
    public CategoryDraft {
        if (name == null) {
            throw new IllegalArgumentException("Name must NOT be null");
        }
    }
}
