package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

public record UpdateCategoryImageCommand(
        CategoryId id,
        Change<CategoryImageKey> imageKey) {
}
