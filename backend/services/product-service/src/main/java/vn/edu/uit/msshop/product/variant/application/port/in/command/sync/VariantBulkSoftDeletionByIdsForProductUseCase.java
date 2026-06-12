package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import java.util.Set;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantBulkSoftDeletionByIdsForProductUseCase {
    void deleteByIds(
            final Set<VariantId> ids);
}
