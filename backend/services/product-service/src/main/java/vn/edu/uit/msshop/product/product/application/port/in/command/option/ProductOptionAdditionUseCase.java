package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionAdditionUseCase {
    ProductView add(
            final AddProductOptionCommand command);
}
