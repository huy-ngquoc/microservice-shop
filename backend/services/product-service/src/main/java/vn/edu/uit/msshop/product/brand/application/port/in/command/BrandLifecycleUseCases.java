package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandCreationCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandHardDeletionCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandInfoUpdateCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationCommand;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandSoftDeletionCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;

public final class BrandLifecycleUseCases {

    private BrandLifecycleUseCases() {
    }

    public interface Create {
        BrandView create(
                final BrandCreationCommand cmd);
    }

    public interface UpdateInfo {
        BrandView updateInfo(
                final BrandInfoUpdateCommand cmd);
    }

    public interface SoftDelete {
        void softDelete(
                final BrandSoftDeletionCommand cmd);
    }

    public interface Restore {
        void restore(
                final BrandRestorationCommand cmd);
    }

    public interface HardDelete {
        void hardDelete(
                final BrandHardDeletionCommand cmd);
    }
}
