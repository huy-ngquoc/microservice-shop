package vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryDeletionByIdPort {
    void deleteById(
            final CategoryId id);
}
