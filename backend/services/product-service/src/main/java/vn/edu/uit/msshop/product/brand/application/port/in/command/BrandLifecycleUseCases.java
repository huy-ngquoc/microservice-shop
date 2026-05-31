package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
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
