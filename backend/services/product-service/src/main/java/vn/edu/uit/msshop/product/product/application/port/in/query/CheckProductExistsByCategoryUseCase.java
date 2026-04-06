package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

public interface CheckProductExistsByCategoryUseCase {
    boolean existsByCategoryId(
            final ProductCategoryId categoryId);
}
