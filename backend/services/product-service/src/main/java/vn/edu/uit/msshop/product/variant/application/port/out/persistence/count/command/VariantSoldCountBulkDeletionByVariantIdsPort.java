package vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantSoldCountBulkDeletionByVariantIdsPort {
    void deleteAllByVariantIds(
            final Collection<VariantId> variantIdCollection);
}
