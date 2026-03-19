package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;

public interface CheckProductExistsPort {
    boolean existsById(
            final ProductId id);
}
