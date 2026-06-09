package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.RemoveProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionRemovalUseCase {
    ProductView remove(
            final RemoveProductOptionCommand command);
}
