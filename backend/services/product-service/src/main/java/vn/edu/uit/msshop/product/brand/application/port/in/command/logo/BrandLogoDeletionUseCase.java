package vn.edu.uit.msshop.product.brand.application.port.in.command.logo;

import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionByIdCommand;

public interface BrandLogoDeletionUseCase {
    void delete(
            final BrandLogoDeletionByIdCommand cmd);
}
