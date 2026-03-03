package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public record UpdateCategoryImageCommand(
        CategoryId id,
        byte[] bytes,
        String originalFilename,
        String contentType) {
}
