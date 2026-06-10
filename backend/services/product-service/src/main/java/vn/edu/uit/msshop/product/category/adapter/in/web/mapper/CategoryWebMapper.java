package vn.edu.uit.msshop.product.category.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryCreationCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryRestorationByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategorySoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.existence.CategoryActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategoryActiveListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategorySoftDeletedListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategoryActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategorySoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Component
@RequiredArgsConstructor
public class CategoryWebMapper {

    private final CategoryLogoUrlResolver urlResolver;

    public CategoryResponse toResponse(
            final CategoryView view) {
        return new CategoryResponse(
                view.id(),
                view.name(),
                this.urlResolver.toLogoUrlString(view.imageKey()),
                view.version());
    }

    public CategoryActiveListingQuery toActiveListingQuery(
            final PageRequestDto pageRequest) {
        return new CategoryActiveListingQuery(
                pageRequest);
    }

    public CategorySoftDeletedListingQuery toSoftDeletedListingQuery(
            final PageRequestDto pageRequest) {
        return new CategorySoftDeletedListingQuery(
                pageRequest);
    }

    public CategoryActiveExistenceCheckByIdQuery toActiveExistenceCheckByIdQuery(
            final UUID categoryId) {
        return new CategoryActiveExistenceCheckByIdQuery(
                categoryId);
    }

    public CategoryActiveLookupByIdQuery toActiveLookupByIdQuery(
            final UUID categoryId) {
        return new CategoryActiveLookupByIdQuery(
                categoryId);
    }

    public CategorySoftDeletedLookupByIdQuery toSoftDeletedLookupByIdQuery(
            final UUID categoryId) {
        return new CategorySoftDeletedLookupByIdQuery(
                categoryId);
    }

    public CategoryCreationCommand toCreateCommand(
            final CreateCategoryRequest request) {
        return new CategoryCreationCommand(
                request.name());
    }

    public CategoryInfoUpdateByIdCommand toInfoUpdateByIdCommand(
            final UUID categoryId,
            final UpdateCategoryInfoRequest request) {
        final var categoryNameChange = ChangeRequest.toChange(request.name());
        return new CategoryInfoUpdateByIdCommand(
                categoryId,
                categoryNameChange,
                request.version());
    }

    public CategorySoftDeletionByIdCommand toSoftDeletionCommand(
            final UUID categoryId,
            final long categoryVersion) {
        return new CategorySoftDeletionByIdCommand(
                categoryId,
                categoryVersion);
    }

    public CategoryRestorationByIdCommand toRestorationCommand(
            final UUID categoryId,
            final long categoryVersion) {
        return new CategoryRestorationByIdCommand(
                categoryId,
                categoryVersion);
    }

    public CategoryHardDeletionByIdCommand toHardDeletionCommand(
            final UUID categoryId,
            final long categoryVersion) {
        return new CategoryHardDeletionByIdCommand(
                categoryId,
                categoryVersion);
    }
}
