package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public interface CheckProductExistsByVariantUseCase {
    boolean existsByVariantId(
            final ProductVariantId variantId);
}
