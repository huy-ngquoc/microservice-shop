package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductSimpleCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductInfoUpdateRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductVariantResponse;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByVariantIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductActiveListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Component
public class ProductWebMapper {

    public ProductResponse toResponse(
            final ProductView view) {
        final var variantsList = new ArrayList<ProductVariantResponse>(view.variants().size());
        for (final var variantView : view.variants()) {
            final var variantResponse = this.toVariantResponse(variantView);
            variantsList.add(variantResponse);
        }

        return new ProductResponse(
                view.id(),
                view.name(),
                view.categoryId(),
                view.brandId(),
                view.minPrice(),
                view.maxPrice(),
                view.soldCount(),
                view.stockCount(),
                view.ratingTotal(),
                view.ratingCount(),
                view.options(),
                variantsList,
                view.imageKeys(),
                view.version());
    }

    public ProductActiveExistenceCheckByIdQuery toActiveExistenceCheckByIdQuery(
            final UUID productId) {
        return new ProductActiveExistenceCheckByIdQuery(
                productId);
    }

    public ProductActiveExistenceCheckByBrandIdQuery toActiveExistenceCheckByBrandIdQuery(
            final UUID brandId) {
        return new ProductActiveExistenceCheckByBrandIdQuery(
                brandId);
    }

    public ProductActiveExistenceCheckByCategoryIdQuery toActiveExistenceCheckByCategoryIdQuery(
            final UUID categoryId) {
        return new ProductActiveExistenceCheckByCategoryIdQuery(
                categoryId);
    }

    public ProductActiveExistenceCheckByVariantIdQuery toActiveExistenceCheckByVariantIdQuery(
            final UUID variantId) {
        return new ProductActiveExistenceCheckByVariantIdQuery(
                variantId);
    }

    public ProductSoftDeletedExistenceCheckByBrandIdQuery toSoftDeletedExistenceCheckByBrandIdQuery(
            final UUID brandId) {
        return new ProductSoftDeletedExistenceCheckByBrandIdQuery(
                brandId);
    }

    public ProductSoftDeletedExistenceCheckByCategoryIdQuery toSoftDeletedExistenceCheckByCategoryIdQuery(
            final UUID categoryId) {
        return new ProductSoftDeletedExistenceCheckByCategoryIdQuery(
                categoryId);
    }

    public ProductActiveListingQuery toActiveListingQuery(
            final PageRequestDto pageRequest) {
        return new ProductActiveListingQuery(
                pageRequest);
    }

    public ProductSoftDeletedListingQuery toSoftDeletedListingQuery(
            final PageRequestDto pageRequest) {
        return new ProductSoftDeletedListingQuery(
                pageRequest);
    }

    public ProductActiveLookupByIdQuery toActiveLookupByIdQuery(
            UUID productId) {
        return new ProductActiveLookupByIdQuery(
                productId);
    }

    public ProductSoftDeletedLookupByIdQuery toSoftDeletedLookupByIdQuery(
            UUID productId) {
        return new ProductSoftDeletedLookupByIdQuery(
                productId);
    }

    public ProductCreationCommand toCreationCommand(
            final ProductCreationRequest request) {
        final var variantList = new ArrayList<NewProductVariantData>(request.variants().size());
        for (final var variantRequest : request.variants()) {
            final var variantData = new NewProductVariantData(
                    variantRequest.price(),
                    variantRequest.traits(),
                    variantRequest.targets());
            variantList.add(variantData);
        }

        return new ProductCreationCommand(
                request.name(),
                request.categoryId(),
                request.brandId(),
                request.options(),
                variantList);
    }

    public ProductSimpleCreationCommand toSimpleCreationCommand(
            final ProductSimpleCreationRequest request) {
        return new ProductSimpleCreationCommand(
                request.name(),
                request.categoryId(),
                request.brandId(),
                request.price(),
                request.targets());
    }

    public ProductRestorationCommand toRestorationCommand(
            final UUID productId,
            final long productVersion) {
        return new ProductRestorationCommand(
                productId,
                productVersion);
    }

    public ProductInfoUpdateCommand toInfoUpdateCommand(
            final UUID productId,
            final ProductInfoUpdateRequest request) {
        final var nameChange = ChangeRequest.toChange(request.name());
        final var categoryIdChange = ChangeRequest.toChange(request.categoryId());
        final var brandIdChange = ChangeRequest.toChange(request.brandId());

        return new ProductInfoUpdateCommand(
                productId,
                nameChange,
                categoryIdChange,
                brandIdChange,
                request.version());
    }

    public ProductSoftDeletionCommand toSoftDeletionCommand(
            final UUID productId,
            final long productVersion) {

        return new ProductSoftDeletionCommand(
                productId,
                productVersion);
    }

    public ProductHardDeletionCommand toHardDeletionCommand(
            final UUID productId,
            final long productVersion) {
        return new ProductHardDeletionCommand(
                productId,
                productVersion);
    }

    private ProductVariantResponse toVariantResponse(
            final ProductVariantView view) {
        return new ProductVariantResponse(
                view.id(),
                view.price(),
                view.traits());
    }
}
