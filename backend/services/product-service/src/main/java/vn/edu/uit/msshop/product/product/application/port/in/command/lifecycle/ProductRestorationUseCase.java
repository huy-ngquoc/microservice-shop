package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationCommand;

public interface ProductRestorationUseCase {
    void restore(
            final ProductRestorationCommand cmd);
}
