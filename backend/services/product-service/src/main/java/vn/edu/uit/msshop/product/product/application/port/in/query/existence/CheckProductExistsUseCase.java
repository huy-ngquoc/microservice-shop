package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface CheckProductExistsUseCase {
    boolean existsById(
            final ProductId id);
}
