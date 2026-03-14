package vn.edu.uit.msshop.product.category.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

@Component
@RequiredArgsConstructor
public class CategoryWebMapper {
    private final Cloudinary cloudinary;

    public CreateCategoryCommand toCommand(
            final CreateCategoryRequest request) {
        final var name = new CategoryName(request.name());
        final var imageKey = this.extractKeyFromTempPublicId(request.imageKey());

        return new CreateCategoryCommand(
                name,
                imageKey);
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

    public UpdateCategoryImageCommand toCommand(
            final UUID id,
            final UpdateCategoryImageRequest request) {
        final var categoryId = new CategoryId(id);

        final var imageKey = ChangeRequest.toChange(request.imageKey(), this::extractKeyFromTempPublicId);

        return new UpdateCategoryImageCommand(
                categoryId,
                imageKey);
    }

    public CategoryId toCategoryId(
            final UUID id) {
        return new CategoryId(id);
    }

    public CategoryResponse toResponse(
            final CategoryView view) {
        final var imageUrl = this.cloudinary.url()
                .generate("categories/" + view.imageKey());

        return new CategoryResponse(
                view.id(),
                view.name(),
                imageUrl);
    }

    private CategoryImageKey extractKeyFromTempPublicId(
            final String publicId) {
        if (!publicId.startsWith("temp/")) {
            throw new IllegalArgumentException("Image key must be in temp folder");
        }

        return new CategoryImageKey(publicId.substring("temp/".length()));
    }
}
