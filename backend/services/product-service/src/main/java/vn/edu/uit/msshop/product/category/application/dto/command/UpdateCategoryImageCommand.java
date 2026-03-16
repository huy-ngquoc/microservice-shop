package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryVersion;

public record UpdateCategoryImageCommand(
        CategoryId id,
        CategoryImageKey newImageKey,
        CategoryVersion expectedVersion) {
}
