package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

public interface CheckProductExistsByCategoryPort {
  boolean existsByCategoryId(final ProductCategoryId categoryId);
}
