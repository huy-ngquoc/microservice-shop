package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

public interface ProductExistenceCheckByVariantIdPort {
    boolean existsByVariantId(
            final ProductVariantId variantId);
}
