package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

public interface DeleteBrandPort {
  void deleteById(final BrandId id);
}
