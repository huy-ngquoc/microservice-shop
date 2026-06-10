package vn.edu.uit.msshop.product.category.application.port.out.validation;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryProductSoftDeletedExistenceCheckByCategoryIdPort {
    boolean existsSoftDeletedByCategoryId(
            final CategoryId categoryId);
}
