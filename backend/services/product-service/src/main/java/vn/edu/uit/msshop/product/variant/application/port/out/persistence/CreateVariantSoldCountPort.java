package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface CreateVariantSoldCountPort {
    VariantSoldCount create(
            final VariantId id);
}
