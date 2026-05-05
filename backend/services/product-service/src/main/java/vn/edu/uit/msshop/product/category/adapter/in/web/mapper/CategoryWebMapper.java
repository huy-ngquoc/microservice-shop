package vn.edu.uit.msshop.product.category.adapter.in.web.mapper;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.adapter.out.image.CategoryImageStorageAdapter;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.DeleteCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.HardDeleteCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.RestoreCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.SoftDeleteCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component
@RequiredArgsConstructor
public class CategoryWebMapper {
  private final CloudinaryImageUrlResolver urlResolver;

  public CreateCategoryCommand toCreateCommand(final CreateCategoryRequest request) {
    final var name = new CategoryName(request.name());

    return new CreateCategoryCommand(name);
  }

  public RestoreCategoryCommand toRestoreCommand(final UUID id, final long expectedVersion) {
    final var categoryId = new CategoryId(id);
    final var version = new CategoryVersion(expectedVersion);

    return new RestoreCategoryCommand(categoryId, version);
  }

  public UpdateCategoryInfoCommand toUpdateInfoCommand(final UUID id,
      final UpdateCategoryInfoRequest request) {
    final var categoryId = new CategoryId(id);
    final var version = new CategoryVersion(request.version());

    final var name = ChangeRequest.toChange(request.name(), CategoryName::new);

    return new UpdateCategoryInfoCommand(categoryId, name, version);
  }

  public UpdateCategoryImageCommand toUpdateImageCommand(final UUID id,
      final UpdateCategoryImageRequest request) {
    final var categoryId = new CategoryId(id);
    final var imageKey = CategoryWebMapper.extractKeyFromTempPublicId(request.newImageKey());
    final var version = new CategoryVersion(request.version());

    return new UpdateCategoryImageCommand(categoryId, imageKey, version);
  }

  public DeleteCategoryImageCommand toDeleteImageCommand(final UUID id,
      final long expectedVersion) {
    final var categoryId = new CategoryId(id);
    final var version = new CategoryVersion(expectedVersion);

    return new DeleteCategoryImageCommand(categoryId, version);
  }

  public SoftDeleteCategoryCommand toSoftDeleteCommand(final UUID id, final long expectedVersion) {
    final var categoryId = new CategoryId(id);
    final var version = new CategoryVersion(expectedVersion);

    return new SoftDeleteCategoryCommand(categoryId, version);
  }

  public HardDeleteCategoryCommand toHardDeleteCommand(final UUID id, final long expectedVersion) {
    final var categoryId = new CategoryId(id);
    final var version = new CategoryVersion(expectedVersion);

    return new HardDeleteCategoryCommand(categoryId, version);
  }

  public CategoryId toCategoryId(final UUID id) {
    return new CategoryId(id);
  }

  public CategoryResponse toResponse(final CategoryView view) {
    return new CategoryResponse(view.id(), view.name(), this.toImageUrlString(view.imageKey()),
        view.version());
  }

  public CategoryImageResponse toImageResponse(final CategoryImageView view) {
    return new CategoryImageResponse(view.id(), this.toImageUrlString(view.imageKey()),
        view.version());
  }

  private static CategoryImageKey extractKeyFromTempPublicId(final String publicId) {
    final var prefix = CloudinaryFolders.TEMP + "/";
    if (!publicId.startsWith(prefix)) {
      throw new IllegalArgumentException("Image key must be in temp folder");
    }

    return new CategoryImageKey(publicId.substring(prefix.length()));
  }

  private @Nullable String toImageUrlString(@Nullable final String keyString) {
    return this.urlResolver.resolve(keyString, CategoryImageStorageAdapter.CATEGORY_FOLDER);
  }
}
