package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionAdditionByIdUseCase {
    ProductView add(
            final ProductOptionAdditionByIdCommand cmd);
}
