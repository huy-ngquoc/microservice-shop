package vn.edu.uit.msshop.product.category.domain.model.mutation;

import lombok.Builder;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Builder
public record CategoryDraft(
        CategoryName name) {
    public CategoryDraft {
        if (name == null) {
            throw new DomainException("Name must NOT be null");
        }
    }
}
