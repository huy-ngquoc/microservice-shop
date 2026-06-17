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
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductInfoUpdateByIdCommand;
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

    public ProductRestorationByIdCommand toRestorationByIdCommand(
            final UUID productId,
            final long productVersion) {
        return new ProductRestorationByIdCommand(
                productId,
                productVersion);
    }

    public ProductInfoUpdateByIdCommand toInfoUpdateCommand(
            final UUID productId,
            final ProductInfoUpdateRequest request) {
        final var nameChange = ChangeRequest.toChange(request.name());
        final var categoryIdChange = ChangeRequest.toChange(request.categoryId());
        final var brandIdChange = ChangeRequest.toChange(request.brandId());

        return new ProductInfoUpdateByIdCommand(
                productId,
                nameChange,
                categoryIdChange,
                brandIdChange,
                request.version());
    }

    public ProductSoftDeletionByIdCommand toSoftDeletionByIdCommand(
            final UUID productId,
            final long productVersion) {

        return new ProductSoftDeletionByIdCommand(
                productId,
                productVersion);
    }

    public ProductHardDeletionByIdCommand toHardDeletionByIdCommand(
            final UUID productId,
            final long productVersion) {
        return new ProductHardDeletionByIdCommand(
                productId,
                productVersion);
    }
}
