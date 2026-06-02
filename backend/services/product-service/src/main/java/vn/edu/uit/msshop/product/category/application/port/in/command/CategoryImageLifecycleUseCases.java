package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryImageLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;

public final class CategoryImageLifecycleUseCases {

    private CategoryImageLifecycleUseCases() {
    }

    public interface Update {
        CategoryImageView update(
                final CategoryImageLifecycleCommands.Update cmd);
    }

    public interface Delete {
        void delete(
                final CategoryImageLifecycleCommands.Delete cmd);
    }
}
