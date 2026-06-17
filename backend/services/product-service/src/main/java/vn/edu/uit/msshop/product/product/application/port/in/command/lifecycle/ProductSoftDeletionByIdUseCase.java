package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionByIdCommand;

public interface ProductSoftDeletionByIdUseCase {
    void softDelete(
            final ProductSoftDeletionByIdCommand cmd);
}
