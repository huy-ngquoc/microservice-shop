package vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandSoftDeletionByIdCommand;

public interface BrandSoftDeletionUseCase {
    void softDelete(
            final BrandSoftDeletionByIdCommand cmd);
}
