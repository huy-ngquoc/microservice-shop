package vn.edu.uit.msshop.product.product.application.port.in;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;

public interface UpdateProductInfoUseCase {
    ProductView updateInfo(
            final UpdateProductInfoCommand command);
}
