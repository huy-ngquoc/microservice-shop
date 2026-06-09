package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantsRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Component
public class ProductVariantWebMapper {

    public AddProductVariantsCommand toAddVariantsCommand(
            final UUID id,
            final AddProductVariantRequest request) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(request.expectedVersion());

        final var variantPrice = new ProductVariantPrice(request.price());
        final var variantTraits = ProductVariantTraits.of(request.traits());
        final var variantTargets = ProductVariantTargets.of(request.targets());
        final var newVariant = new NewProductVariant(
                variantPrice,
                variantTraits,
                variantTargets);
        final var newVariants = new NewProductVariants(List.of(newVariant));

        return new AddProductVariantsCommand(
                productId,
                newVariants,
                version);
    }

    public AddProductVariantsCommand toAddVariantsCommand(
            final UUID id,
            final AddProductVariantsRequest request) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(request.expectedVersion());

        final var newVariantsList = request.variants().stream()
                .map(ProductVariantWebMapper::toNewVariant)
                .toList();
        final var newVariants = new NewProductVariants(newVariantsList);

        return new AddProductVariantsCommand(
                productId,
                newVariants,
                version);
    }

    private static NewProductVariant toNewVariant(
            final AddProductVariantsRequest.ProductVariantRequest request) {
        final var variantPrice = new ProductVariantPrice(request.price());
        final var variantTraits = ProductVariantTraits.of(request.traits());
        final var variantTargets = ProductVariantTargets.of(request.targets());

        return new NewProductVariant(
                variantPrice,
                variantTraits,
                variantTargets);
    }
}
