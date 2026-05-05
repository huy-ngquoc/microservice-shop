package vn.edu.uit.msshop.product.category.application.port.out.validation;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CheckCategoryHasProductsPort {
  boolean hasProduct(final CategoryId id);
}
