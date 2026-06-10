package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductExistenceCheckByIdPort {
    boolean existsById(
            final ProductId id);
}
