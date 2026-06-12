package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public interface VariantBulkRestorationByIdsForProductUseCase {
    void restoreByIds(
            final Collection<VariantId> ids);
}
