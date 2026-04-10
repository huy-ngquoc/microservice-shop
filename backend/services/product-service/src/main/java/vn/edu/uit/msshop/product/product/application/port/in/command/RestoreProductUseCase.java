package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.RestoreProductCommand;

public interface RestoreProductUseCase {
    void restore(
            final RestoreProductCommand command);
}
