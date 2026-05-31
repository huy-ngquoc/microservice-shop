package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public interface CreateBrandUseCase {
    BrandView create(
            final BrandLifecycleCommands.Create command);
}
