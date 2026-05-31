package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public interface UpdateBrandLogoUseCase {
    BrandLogoView updateLogo(
            final BrandLifecycleCommands.UpdateLogo command);
}
