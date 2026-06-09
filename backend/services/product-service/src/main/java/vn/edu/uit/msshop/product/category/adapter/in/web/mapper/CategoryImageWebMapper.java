package vn.edu.uit.msshop.product.category.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.application.dto.command.image.CategoryImageDeletionByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.image.CategoryImageUpdateByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryPublicIds;

@Component
@RequiredArgsConstructor
public class CategoryImageWebMapper {

    private final CategoryLogoUrlResolver urlResolver;

    public CategoryImageUpdateByIdCommand toImageUpdateByIdCommand(
            final UUID categoryId,
            final UpdateCategoryImageRequest request) {
        final var imageKey = CloudinaryPublicIds.extractKeyFromTemp(request.newImageKey());
        return new CategoryImageUpdateByIdCommand(
                categoryId,
                imageKey,
                request.version());
    }

    public CategoryImageDeletionByIdCommand toImageDeletionByIdCommand(
            final UUID categoryId,
            final long categoryVersion) {
        return new CategoryImageDeletionByIdCommand(
                categoryId,
                categoryVersion);
    }

    public CategoryId toCategoryId(
            final UUID id) {
        return new CategoryId(id);
    }

    public CategoryImageResponse toResponse(
            final CategoryImageView view) {
        return new CategoryImageResponse(
                view.id(),
                this.urlResolver.toLogoUrlString(view.imageKey()),
                view.version());
    }
}
