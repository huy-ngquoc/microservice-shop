package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.IncreaseProductStockCountsForVariantsCommand;

public interface ProductStockCountIncreaseForVariantsUseCase {
    void increase(
            final IncreaseProductStockCountsForVariantsCommand command);
}
