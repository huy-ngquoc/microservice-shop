package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountIncreaseForVariantsCommand;

public interface ProductStockCountIncreaseForVariantsUseCase {
    void increase(
            final ProductStockCountIncreaseForVariantsCommand cmd);
}
