package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;

public interface AddProductVariantsUseCase {
    ProductView addVariants(
            final AddProductVariantsCommand command);
}
