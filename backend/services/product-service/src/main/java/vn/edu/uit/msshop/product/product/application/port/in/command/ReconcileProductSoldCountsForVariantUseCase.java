package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileProductSoldCountsForVariantCommand;

public interface ReconcileProductSoldCountsForVariantUseCase {
    void execute(
            final ReconcileProductSoldCountsForVariantCommand command);
}
