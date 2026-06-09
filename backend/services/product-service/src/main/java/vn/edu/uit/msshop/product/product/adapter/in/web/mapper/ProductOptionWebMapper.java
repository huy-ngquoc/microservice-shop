package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.RemoveProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductOptionRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionUpdateCommand;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Component
public class ProductOptionWebMapper {
    public ProductOptionAdditionCommand toAdditionCommand(
            final UUID id,
            final AddProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var defaultTrait = new ProductVariantTrait(request.defaultTrait());
        final var version = new ProductVersion(request.expectedVersion());

        return new ProductOptionAdditionCommand(
                productId,
                option,
                defaultTrait,
                version);
    }

    public ProductOptionUpdateCommand toUpdateCommand(
            final UUID id,
            final int index,
            final UpdateProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var version = new ProductVersion(request.expectedVersion());

        return new ProductOptionUpdateCommand(
                productId,
                index,
                option,
                version);
    }

    public ProductOptionRemovalCommand toRemovalCommand(
            final UUID id,
            final int index,
            final RemoveProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var defaultPrice = ProductPrice.ofNullable(request.defaultPrice());
        final var version = new ProductVersion(request.expectedVersion());

        return new ProductOptionRemovalCommand(
                productId,
                index,
                defaultPrice,
                version);
    }
}
