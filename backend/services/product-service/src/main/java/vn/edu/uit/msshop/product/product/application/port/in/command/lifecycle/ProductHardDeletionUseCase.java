package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionCommand;

public interface ProductHardDeletionUseCase {
    void hardDelete(
            final ProductHardDeletionCommand cmd);
}
