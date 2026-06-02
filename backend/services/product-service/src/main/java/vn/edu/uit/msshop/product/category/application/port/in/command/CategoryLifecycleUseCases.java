package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public final class CategoryLifecycleUseCases {

    private CategoryLifecycleUseCases() {
    }

    public interface Create {
        CategoryView create(
                final CategoryLifecycleCommands.Create cmd);
    }

    public interface UpdateInfo {
        CategoryView updateInfo(
                final CategoryLifecycleCommands.UpdateInfo cmd);
    }

    public interface SoftDelete {
        void softDelete(
                final CategoryLifecycleCommands.SoftDelete cmd);
    }

    public interface Restore {
        void restore(
                final CategoryLifecycleCommands.Restore cmd);
    }

    public interface HardDelete {
        void hardDelete(
                final CategoryLifecycleCommands.HardDelete cmd);
    }
}
