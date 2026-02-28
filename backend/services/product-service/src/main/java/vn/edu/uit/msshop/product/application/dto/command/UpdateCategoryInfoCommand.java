package vn.edu.uit.msshop.product.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.application.common.Change;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@NullMarked
public record UpdateCategoryInfoCommand(
        CategoryId id,

        Change<CategoryName> name) {
}
