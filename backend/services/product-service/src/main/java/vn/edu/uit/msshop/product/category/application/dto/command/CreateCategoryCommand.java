package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.CategoryName;

public record CreateCategoryCommand(
        CategoryName name) {
}
