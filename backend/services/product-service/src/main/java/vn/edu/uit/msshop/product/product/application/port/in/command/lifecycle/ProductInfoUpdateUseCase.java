package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductInfoUpdateUseCase {
    ProductView updateInfo(
            final ProductInfoUpdateCommand cmd);
}
