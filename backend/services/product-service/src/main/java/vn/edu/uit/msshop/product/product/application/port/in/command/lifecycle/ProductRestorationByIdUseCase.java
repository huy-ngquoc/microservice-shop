package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationByIdCommand;

public interface ProductRestorationByIdUseCase {
    void restore(
            final ProductRestorationByIdCommand cmd);
}
