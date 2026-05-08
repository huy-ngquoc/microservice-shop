package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductStockCountsForVariantsCommand;

public interface DecreaseProductStockCountsForVariantsUseCase {
    void execute(
            final DecreaseProductStockCountsForVariantsCommand command);
}
