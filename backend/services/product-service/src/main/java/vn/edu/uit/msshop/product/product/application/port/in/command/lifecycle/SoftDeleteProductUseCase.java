package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.SoftDeleteProductCommand;

public interface SoftDeleteProductUseCase {
    void delete(
            final SoftDeleteProductCommand command);
}
