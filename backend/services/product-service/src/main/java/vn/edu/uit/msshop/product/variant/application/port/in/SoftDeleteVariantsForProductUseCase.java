package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

public interface SoftDeleteVariantsForProductUseCase {
    void deleteByProductId(
            final VariantProductId productId);
}
