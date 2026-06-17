package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductInfoUpdateByIdUseCase {
    ProductView updateInfo(
            final ProductInfoUpdateByIdCommand cmd);
}
