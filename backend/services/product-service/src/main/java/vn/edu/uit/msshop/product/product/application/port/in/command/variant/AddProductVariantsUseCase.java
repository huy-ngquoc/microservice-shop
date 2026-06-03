package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface AddProductVariantsUseCase {
    ProductView addVariants(
            final AddProductVariantsCommand command);
}
