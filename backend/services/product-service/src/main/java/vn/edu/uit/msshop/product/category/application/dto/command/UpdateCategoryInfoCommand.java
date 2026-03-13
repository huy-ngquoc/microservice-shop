package vn.edu.uit.msshop.product.category.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@NullMarked
public record UpdateCategoryInfoCommand(
        CategoryId id,
        Change<CategoryName> name) {
}
