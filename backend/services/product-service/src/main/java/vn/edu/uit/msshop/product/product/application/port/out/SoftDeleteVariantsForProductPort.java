package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;

public interface SoftDeleteVariantsForProductPort {
    void deleteByProductId(
            final ProductId id);
}
