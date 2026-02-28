package vn.edu.uit.msshop.product.application.dto.command;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

public record CreateCategoryCommand(
        CategoryName name,

        @Nullable
        CategoryImage image) {
}
