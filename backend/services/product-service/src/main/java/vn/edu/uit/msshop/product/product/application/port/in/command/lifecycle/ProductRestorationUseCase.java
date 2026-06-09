package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.RestoreProductCommand;

public interface ProductRestorationUseCase {
    void restore(
            final RestoreProductCommand command);
}
