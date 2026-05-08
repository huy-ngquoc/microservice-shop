package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;

public record UpdateCategoryImageCommand(
        CategoryId id,
        CategoryImageKey newImageKey,
        CategoryVersion expectedVersion) {
}
