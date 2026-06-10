package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductSimpleCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductInfoUpdateRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateCommand;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

@Component
public class ProductCommandWebMapper {

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
}
