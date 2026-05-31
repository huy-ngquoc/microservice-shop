package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.RemoveProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductOptionRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Component
public class ProductOptionWebMapper {
    public AddProductOptionCommand toAddOptionCommand(
            final UUID id,
            final AddProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var defaultTrait = new ProductVariantTrait(request.defaultTrait());
        final var version = new ProductVersion(request.expectedVersion());

        return new AddProductOptionCommand(
                productId,
                option,
                defaultTrait,
                version);
    }

    public UpdateProductOptionCommand toUpdateOptionCommand(
            final UUID id,
            final int index,
            final UpdateProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var version = new ProductVersion(request.expectedVersion());

        return new UpdateProductOptionCommand(
                productId,
                index,
                option,
                version);
    }

    public RemoveProductOptionCommand toRemoveOptionCommand(
            final UUID id,
            final int index,
            final RemoveProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var defaultPrice = ProductPrice.ofNullable(request.defaultPrice());
        final var version = new ProductVersion(request.expectedVersion());

        return new RemoveProductOptionCommand(
                productId,
                index,
                defaultPrice,
                version);
    }
}
