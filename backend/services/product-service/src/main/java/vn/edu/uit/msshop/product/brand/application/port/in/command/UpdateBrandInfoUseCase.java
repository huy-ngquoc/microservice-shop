package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface UpdateBrandInfoUseCase {
    BrandView updateInfo(
            final BrandLifecycleCommands.UpdateInfo command);
}
