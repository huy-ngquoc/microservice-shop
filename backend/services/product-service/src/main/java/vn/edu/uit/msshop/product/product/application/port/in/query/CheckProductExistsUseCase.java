package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface CheckProductExistsUseCase {
    boolean existsById(
            final ProductId id);
}
