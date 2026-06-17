package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionByIdCommand;

public interface ProductHardDeletionByIdUseCase {
    void hardDelete(
            final ProductHardDeletionByIdCommand cmd);
}
