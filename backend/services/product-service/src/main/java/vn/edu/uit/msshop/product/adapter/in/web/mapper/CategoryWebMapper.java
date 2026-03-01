package vn.edu.uit.msshop.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.adapter.in.web.request.common.ChangeRequest;
import vn.edu.uit.msshop.product.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryName;

@Component
public class CategoryWebMapper {
    public CreateCategoryCommand toCommand(
            final CreateCategoryRequest request) {
        final var name = new CategoryName(request.name());

        return new CreateCategoryCommand(name);
    }

    public UpdateCategoryInfoCommand toCommand(
            final UUID id,
            final UpdateCategoryInfoRequest request) {
        final var categoryId = new CategoryId(id);

        final var name = ChangeRequest.toChange(request.name(), CategoryName::new);

        return new UpdateCategoryInfoCommand(
                categoryId,
                name);
    }

    public CategoryResponse toResponse(
            final CategoryView view) {
        return new CategoryResponse(
                view.id(),
                view.name(),
                view.imageUrl());
    }

    public CategoryImageResponse toResponse(
            final CategoryImageView view) {
        return new CategoryImageResponse(
                view.url(),
                view.width(),
                view.height());
    }
}
