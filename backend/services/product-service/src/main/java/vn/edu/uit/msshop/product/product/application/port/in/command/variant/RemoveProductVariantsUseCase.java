package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface RemoveProductVariantsUseCase {
    ProductView removeVariants(
            final RemoveProductVariantsCommand command);
}
