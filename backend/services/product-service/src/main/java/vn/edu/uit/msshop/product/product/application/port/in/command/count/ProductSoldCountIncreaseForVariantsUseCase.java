package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.IncreaseProductSoldCountsForVariantsCommand;

public interface ProductSoldCountIncreaseForVariantsUseCase {
    void increase(
            final IncreaseProductSoldCountsForVariantsCommand command);
}
