package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public interface CheckProductExistsByVariantPort {
    boolean existsByVariantId(
            final ProductVariantId variantId);
}
