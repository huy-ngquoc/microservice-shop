package vn.edu.uit.msshop.product.brand.application.port.in;

import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;

public interface UpdateBrandInfoUseCase {
    void updateInfo(
            final UpdateBrandInfoCommand command);
}
