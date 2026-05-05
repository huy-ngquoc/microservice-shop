package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface CheckProductExistsByBrandPort {
  boolean existsByBrandId(final ProductBrandId brandId);
}
