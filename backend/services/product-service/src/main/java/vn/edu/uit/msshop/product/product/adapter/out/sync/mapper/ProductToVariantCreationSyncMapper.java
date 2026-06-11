package vn.edu.uit.msshop.product.product.adapter.out.sync.mapper;

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
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Component
public class ProductToVariantCreationSyncMapper {

    public CreateVariantsForNewProductCommand toCreateCommand(
            final ProductId id,
            final ProductName name,
            final NewProductVariants newVariants) {
        final var variantProductId = new VariantProductId(id.value());
        final var variantProductName = new VariantProductName(name.value());
        final var newVariantInputsList = newVariants.values().stream()
                .map(ProductToVariantCreationSyncMapper::toNewVariantInput)
                .toList();
        final var newVariantInputs = new NewVariantsForNewProduct(newVariantInputsList);

        return new CreateVariantsForNewProductCommand(
                variantProductId,
                variantProductName,
                newVariantInputs);
    }

    public ProductVariants toProductVariants(
            final Collection<VariantView> variantViewList) {
        final var productVariantList = variantViewList.stream()
                .map(ProductToVariantCreationSyncMapper::toProductVariant)
                .toList();
        return new ProductVariants(productVariantList);
    }

    private static NewVariantForNewProduct toNewVariantInput(
            final NewProductVariant variant) {
        final var price = new VariantPrice(variant.price().value());
        final var traits = VariantTraits.of(variant.traits().unwrap());
        final var targets = VariantTargets.of(variant.targets().unwrap());

        return new NewVariantForNewProduct(price, traits, targets);
    }

    private static ProductVariant toProductVariant(
            final VariantView variantView) {
        final var id = new ProductVariantId(variantView.id());
        final var price = new ProductVariantPrice(variantView.price());
        final var traits = ProductVariantTraits.of(variantView.traits());

        return new ProductVariant(id, price, traits);
    }
}
