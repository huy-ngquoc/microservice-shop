package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductStockCountsForVariantsCommand;

public interface IncreaseProductStockCountsForVariantsUseCase {
    void execute(
            final IncreaseProductStockCountsForVariantsCommand command);
}
