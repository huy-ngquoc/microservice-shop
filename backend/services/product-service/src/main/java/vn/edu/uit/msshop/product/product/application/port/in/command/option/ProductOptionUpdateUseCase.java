package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionUpdateUseCase {
    ProductView update(
            final UpdateProductOptionCommand command);
}
