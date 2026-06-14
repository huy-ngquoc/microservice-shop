package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantSoldCountLookupByVariantIdPort {
    Optional<VariantSoldCount> loadByVariantId(
            final VariantId variantId);

    default VariantSoldCount loadByVariantIdOrZero(
            final VariantId variantId,
            final VariantProductId productId) {
        return this.loadByVariantId(variantId).orElse(VariantSoldCount.zero(variantId, productId));
    }
}
