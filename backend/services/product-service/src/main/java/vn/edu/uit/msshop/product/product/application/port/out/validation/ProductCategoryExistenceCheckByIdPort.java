package vn.edu.uit.msshop.product.product.application.port.out.validation;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

public interface ProductCategoryExistenceCheckByIdPort {
    boolean existsById(
            final ProductCategoryId categoryId);
}
