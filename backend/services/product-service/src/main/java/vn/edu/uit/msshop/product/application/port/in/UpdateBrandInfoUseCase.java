package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandInfoCommand;

public interface UpdateBrandInfoUseCase {
    void updateInfo(
            final UpdateBrandInfoCommand command);
}
