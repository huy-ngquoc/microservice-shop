package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantActiveCountByProductIdPort {
    long countActiveByProductId(
            final VariantProductId productId);
}
