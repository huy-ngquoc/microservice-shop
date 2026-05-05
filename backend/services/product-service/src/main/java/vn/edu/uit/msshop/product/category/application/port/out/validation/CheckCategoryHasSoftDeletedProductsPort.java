package vn.edu.uit.msshop.product.category.application.port.out.validation;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CheckCategoryHasSoftDeletedProductsPort {
  boolean hasSoftDeletedProduct(final CategoryId id);
}
