package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;

public interface UpdateProductOptionUseCase {
    ProductView updateOption(
            final UpdateProductOptionCommand command);
}
