package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoldCountDeletionByVariantIdPort {
    void deleteByVariantId(
            final VariantId variantId);
}
