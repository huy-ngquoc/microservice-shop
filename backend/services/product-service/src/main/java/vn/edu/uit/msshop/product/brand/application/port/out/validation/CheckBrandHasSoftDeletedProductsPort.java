package vn.edu.uit.msshop.product.brand.application.port.out.validation;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface CheckBrandHasSoftDeletedProductsPort {
  boolean hasSoftDeletedProduct(final BrandId brandId);
}
