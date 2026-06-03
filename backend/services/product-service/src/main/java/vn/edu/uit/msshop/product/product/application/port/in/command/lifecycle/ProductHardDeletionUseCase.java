package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.HardDeleteProductCommand;

public interface ProductHardDeletionUseCase {
    void purge(
            final HardDeleteProductCommand command);
}
