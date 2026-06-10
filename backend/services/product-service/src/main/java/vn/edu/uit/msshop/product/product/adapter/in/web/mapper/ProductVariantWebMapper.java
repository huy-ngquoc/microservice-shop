package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantsRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantBulkAdditionCommand;

@Component
public class ProductVariantWebMapper {

    public ProductVariantBulkAdditionCommand toBulkAdditionCommand(
            final UUID productId,
            final AddProductVariantRequest request) {
        final var newVariant = new NewProductVariantData(
                request.price(),
                request.traits(),
                request.targets());
        final var newVariantList = List.of(newVariant);

        return new ProductVariantBulkAdditionCommand(
                productId,
                newVariantList,
                request.version());
    }

    public ProductVariantBulkAdditionCommand toBulkAdditionCommand(
            final UUID productId,
            final AddProductVariantsRequest request) {
        final var newVariantList = new ArrayList<NewProductVariantData>(request.variants().size());
        for (final var variantRequest : request.variants()) {
            final var newVariant = new NewProductVariantData(
                    variantRequest.price(),
                    variantRequest.traits(),
                    variantRequest.targets());
            newVariantList.add(newVariant);
        }

        return new ProductVariantBulkAdditionCommand(
                productId,
                newVariantList,
                request.version());
    }
}
