package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

public interface ProductSoftDeletedExistenceCheckByCategoryIdPort {
    boolean existsSoftDeletedByCategoryId(
            final ProductCategoryId categoryId);
}
