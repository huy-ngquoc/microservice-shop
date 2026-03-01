package vn.edu.uit.msshop.product.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

public record CreateCategoryCommand(
        CategoryName name) {
}
