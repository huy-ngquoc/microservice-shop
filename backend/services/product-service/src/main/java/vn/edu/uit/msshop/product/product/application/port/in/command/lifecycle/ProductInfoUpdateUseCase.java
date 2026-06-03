package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductInfoUpdateUseCase {
    ProductView updateInfo(
            final UpdateProductInfoCommand command);
}
