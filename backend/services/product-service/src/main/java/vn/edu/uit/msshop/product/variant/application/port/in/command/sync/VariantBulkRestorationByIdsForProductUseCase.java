package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;

public interface VariantBulkRestorationByIdsForProductUseCase {
    void restoreByIds(
            final VariantBulkRestorationByIdsForProductCommand cmd);
}
