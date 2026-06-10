package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductSoldCountsForVariantsCommand;

public interface ProductSoldCountDecreaseForVariantsUseCase {
    void decrease(
            final DecreaseProductSoldCountsForVariantsCommand command);
}
