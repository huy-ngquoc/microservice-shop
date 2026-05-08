package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.HardDeleteProductCommand;

public interface HardDeleteProductUseCase {
    void purge(
            final HardDeleteProductCommand command);
}
