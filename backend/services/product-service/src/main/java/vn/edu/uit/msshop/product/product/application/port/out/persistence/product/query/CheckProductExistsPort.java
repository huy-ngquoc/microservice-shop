package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

// TODO: Should be "CheckProductExistsByIdPort"?
// Because there is "CheckProductExistsByBrandPort".
public interface CheckProductExistsPort {
    boolean existsById(
            final ProductId id);
}
