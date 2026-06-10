package vn.edu.uit.msshop.product.category.application.port.in.command.image;

import vn.edu.uit.msshop.product.category.application.dto.command.image.CategoryImageDeletionByIdCommand;

public interface CategoryImageDeletionByIdUseCase {
    void delete(
            final CategoryImageDeletionByIdCommand cmd);
}
