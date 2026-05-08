package vn.edu.uit.msshop.product.category.application.port.out.persistence;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface DeleteCategoryPort {
    void deleteById(
            final CategoryId id);
}
