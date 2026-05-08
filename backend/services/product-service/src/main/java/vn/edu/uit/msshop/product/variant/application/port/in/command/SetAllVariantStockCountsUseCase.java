package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantStockCountsCommand;

public interface SetAllVariantStockCountsUseCase {
    void execute(
            final SetAllVariantStockCountsCommand command);
}
