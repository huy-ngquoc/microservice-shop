package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;

public interface RemoveProductOptionUseCase {
    ProductView removeOption(
            final RemoveProductOptionCommand command);
}
