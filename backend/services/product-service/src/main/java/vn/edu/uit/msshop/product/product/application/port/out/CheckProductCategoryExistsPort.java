package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;

public interface CheckProductCategoryExistsPort {
    boolean existsById(
            final ProductCategoryId categoryId);
}
