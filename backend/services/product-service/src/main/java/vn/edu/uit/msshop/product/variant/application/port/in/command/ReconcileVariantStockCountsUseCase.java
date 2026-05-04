package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileVariantStockCountsCommand;

public interface ReconcileVariantStockCountsUseCase {
    void execute(
            final ReconcileVariantStockCountsCommand command);
}
