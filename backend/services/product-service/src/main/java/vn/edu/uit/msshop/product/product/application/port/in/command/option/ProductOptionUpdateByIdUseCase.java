package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionUpdateByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionUpdateByIdUseCase {
    ProductView update(
            final ProductOptionUpdateByIdCommand command);
}
