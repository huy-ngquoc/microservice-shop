package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountDecreaseForVariantsCommand;

public interface ProductStockCountDecreaseForVariantsUseCase {
    void decrease(
            final ProductStockCountDecreaseForVariantsCommand command);
}
