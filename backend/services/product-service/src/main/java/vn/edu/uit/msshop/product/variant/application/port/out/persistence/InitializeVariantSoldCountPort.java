package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface InitializeVariantSoldCountPort {
    VariantSoldCount initialize(
            final VariantId id,
            final VariantProductId productId);
}
