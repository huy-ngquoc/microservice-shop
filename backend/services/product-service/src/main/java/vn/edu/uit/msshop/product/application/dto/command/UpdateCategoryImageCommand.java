package vn.edu.uit.msshop.product.application.dto.command;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public record UpdateCategoryImageCommand(
        CategoryId id,
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
