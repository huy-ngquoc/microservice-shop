package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.variant.application.dto.command.RemoveProductOptionCommand;

public interface RemoveProductOptionUseCase {
    ProductView removeOption(
            final RemoveProductOptionCommand command);
}
