package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.CreateBrandCommand;

public interface CreateBrandUseCase {
    void create(
            final CreateBrandCommand command);
}
