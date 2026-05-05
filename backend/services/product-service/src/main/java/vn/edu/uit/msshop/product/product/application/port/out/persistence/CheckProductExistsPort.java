package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

// TODO: Should be "CheckProductExistsByIdPort"?
// Because there is "CheckProductExistsByBrandPort".
public interface CheckProductExistsPort {
  boolean existsById(final ProductId id);
}
