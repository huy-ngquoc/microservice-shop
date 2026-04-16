package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantCommand;

public interface IncreaseProductSoldCountsForVariantUseCase {
    void execute(
            final IncreaseProductSoldCountsForVariantCommand command);
}
