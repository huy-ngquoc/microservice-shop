package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateSimpleProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductVariantResponse;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

@Component
public class ProductWebMapper {

    public ProductId toProductId(
            final UUID id) {
        return new ProductId(id);
    }

    public ProductCreationCommand toCreationCommand(
            final CreateProductRequest request) {
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
            final CreateSimpleProductRequest request) {
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
            final UpdateProductInfoRequest request) {
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

    private ProductVariantResponse toVariantResponse(
            final ProductVariantView view) {
        return new ProductVariantResponse(
                view.id(),
                view.price(),
                view.traits());
    }
}
