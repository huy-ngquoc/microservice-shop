package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

public interface ProductSoftDeletedExistenceCheckByCategoryIdUseCase {
    boolean existsSoftDeletedByCategoryId(
            final ProductCategoryId categoryId);
}
