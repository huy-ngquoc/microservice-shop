package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public final class BrandLifecycleUseCases {
    private BrandLifecycleUseCases() {
    }

    public interface Create {
        BrandView create(
                final BrandLifecycleCommands.Create cmd);
    }

    public interface UpdateInfo {
        BrandView updateInfo(
                final BrandLifecycleCommands.UpdateInfo cmd);
    }

    public interface UpdateLogo {
        BrandLogoView updateLogo(
                final BrandLifecycleCommands.UpdateLogo cmd);
    }

    public interface DeleteLogo {
        void deleteLogo(
                final BrandLifecycleCommands.DeleteLogo cmd);
    }

    public interface SoftDelete {
        void softDelete(
                final BrandLifecycleCommands.SoftDelete cmd);
    }

    public interface Restore {
        void restore(
                final BrandLifecycleCommands.Restore cmd);
    }

    public interface HardDelete {
        void hardDelete(
                final BrandLifecycleCommands.HardDelete cmd);
    }
}
