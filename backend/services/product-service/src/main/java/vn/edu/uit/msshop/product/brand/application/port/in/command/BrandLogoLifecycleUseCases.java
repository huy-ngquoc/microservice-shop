package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoUpdateCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;

public final class BrandLogoLifecycleUseCases {

    private BrandLogoLifecycleUseCases() {
    }

    public interface Update {
        BrandLogoView update(
                final BrandLogoUpdateCommand cmd);
    }

    public interface Delete {
        void delete(
                final BrandLogoDeletionCommand cmd);
    }
}
