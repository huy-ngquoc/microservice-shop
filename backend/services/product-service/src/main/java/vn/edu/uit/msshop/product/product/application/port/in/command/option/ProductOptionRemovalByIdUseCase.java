package vn.edu.uit.msshop.product.product.application.port.in.command.option;

import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductOptionRemovalByIdUseCase {
    ProductView remove(
            final ProductOptionRemovalByIdCommand cmd);
}
