package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query;

import java.util.Optional;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantSoldCountLookupByIdPort {
    Optional<VariantSoldCount> loadById(
            final VariantId id);

    default VariantSoldCount loadByIdOrZero(
            final VariantId id,
            final VariantProductId productId) {
        return this.loadById(id).orElse(VariantSoldCount.zero(id, productId));
    }
}
