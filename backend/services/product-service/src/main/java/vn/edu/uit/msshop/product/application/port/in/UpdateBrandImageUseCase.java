package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandImageCommand;
import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;

public interface UpdateBrandImageUseCase {
    BrandLogoView updateImage(
            final UpdateBrandImageCommand command);
}
