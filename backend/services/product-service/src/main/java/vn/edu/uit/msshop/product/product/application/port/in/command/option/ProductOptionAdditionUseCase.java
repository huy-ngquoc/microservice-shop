package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionAdditionUseCase {
    ProductView add(
            final ProductOptionAdditionCommand cmd);
}
