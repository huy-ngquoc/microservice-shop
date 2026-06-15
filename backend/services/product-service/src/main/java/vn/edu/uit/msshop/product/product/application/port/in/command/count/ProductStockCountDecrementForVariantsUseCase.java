package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountDecreaseForVariantsCommand;

public interface ProductStockCountDecrementForVariantsUseCase {
    void decrease(
            final ProductStockCountDecreaseForVariantsCommand cmd);
}
