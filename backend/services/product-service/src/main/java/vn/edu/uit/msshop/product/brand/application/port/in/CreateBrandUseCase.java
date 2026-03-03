package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;

public interface CreateBrandUseCase {
    void create(
            final CreateBrandCommand command);
}
