package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLogoLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public final class BrandLogoLifecycleUseCases {
    private BrandLogoLifecycleUseCases() {
    }

    public interface Update {
        BrandLogoView update(
                final BrandLogoLifecycleCommands.Update cmd);
    }

    public interface Delete {
        void delete(
                final BrandLogoLifecycleCommands.Delete cmd);
    }
}
