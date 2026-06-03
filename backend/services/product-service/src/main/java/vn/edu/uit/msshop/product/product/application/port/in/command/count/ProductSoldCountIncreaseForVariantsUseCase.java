package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.IncreaseProductSoldCountsForVariantsCommand;

public interface ProductSoldCountIncreaseForVariantsUseCase {
    void execute(
            final IncreaseProductSoldCountsForVariantsCommand command);
}
