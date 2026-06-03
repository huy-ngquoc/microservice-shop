package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionUpdateUseCase {
    ProductView updateOption(
            final UpdateProductOptionCommand command);
}
