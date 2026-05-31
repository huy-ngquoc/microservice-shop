package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public final class BrandLogoLifecycleUseCases {
    private BrandLogoLifecycleUseCases() {
    }

    public interface UpdateLogo {
        BrandLogoView updateLogo(
                final BrandLifecycleCommands.UpdateLogo cmd);
    }

    public interface DeleteLogo {
        void deleteLogo(
                final BrandLifecycleCommands.DeleteLogo cmd);
    }
}
