package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountDecreaseForVariantsCommand;

public interface ProductSoldCountDecreaseForVariantsUseCase {
    void decrease(
            final ProductSoldCountDecreaseForVariantsCommand cmd);
}
