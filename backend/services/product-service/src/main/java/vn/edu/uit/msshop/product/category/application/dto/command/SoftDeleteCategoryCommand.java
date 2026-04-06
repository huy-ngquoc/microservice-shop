package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;

public record SoftDeleteCategoryCommand(
        CategoryId id,
        CategoryVersion expectedVersion) {
}
