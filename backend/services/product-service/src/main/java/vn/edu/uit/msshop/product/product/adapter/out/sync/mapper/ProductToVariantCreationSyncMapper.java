package vn.edu.uit.msshop.product.product.adapter.out.sync.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.dto.command.data.NewVariantForNewProductData;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkCreationForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

@Component
public class ProductToVariantCreationSyncMapper {

    public VariantBulkCreationForNewProductCommand toCreateCommand(
            final ProductId id,
            final ProductName name,
            final NewProductVariants newVariants) {
        final var newVariantList = newVariants.values();
        final var amountVariant = newVariantList.size();
        final var newVariantDataList = new ArrayList<NewVariantForNewProductData>(
                amountVariant);

        for (final var variant : newVariantList) {
            final var newVariantData = ProductToVariantCreationSyncMapper.toNewVariantData(variant);
            newVariantDataList.add(newVariantData);
        }

        return new VariantBulkCreationForNewProductCommand(
                id.value(),
                name.value(),
                newVariantDataList);
    }

    public ProductVariants toProductVariants(
            final Collection<VariantView> variantViewList) {
        final var productVariantList = variantViewList.stream()
                .map(ProductToVariantCreationSyncMapper::toProductVariant)
                .toList();
        return new ProductVariants(productVariantList);
    }

    private static NewVariantForNewProductData toNewVariantData(
            final NewProductVariant variant) {
        return new NewVariantForNewProductData(
                variant.price().value(),
                variant.traits().unwrap(),
                variant.targets().unwrap());
    }

    private static ProductVariant toProductVariant(
            final VariantView variantView) {
        final var id = new ProductVariantId(variantView.id());
        final var price = new ProductVariantPrice(variantView.price());
        final var traits = ProductVariantTraits.of(variantView.traits());

        return new ProductVariant(id, price, traits);
    }
}
