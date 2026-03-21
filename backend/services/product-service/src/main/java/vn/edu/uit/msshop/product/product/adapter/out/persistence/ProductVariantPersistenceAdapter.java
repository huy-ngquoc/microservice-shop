package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.CreateProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

@Component
@RequiredArgsConstructor
public class ProductVariantPersistenceAdapter
        implements CreateProductVariantsPort {
    private final CreateVariantsForNewProductUseCase createorNewProductUseCase;

    @Override
    public ProductVariants create(
            final ProductId id,
            final NewProductVariants newVariants) {
        final var variantProductId = new VariantProductId(id.value());
        final var newVariantInputsList = newVariants.values().stream()
                .map(this::toNewVariantInput).toList();
        final var newVariantInputs = new NewVariantsForNewProduct(newVariantInputsList);

        final var command = new CreateVariantsForNewProductCommand(
                variantProductId,
                newVariantInputs);

        final var variantViewsList = this.createorNewProductUseCase.create(command);

        final var productVariantsList = variantViewsList.stream()
                .map(this::toProductVariant).toList();
        return new ProductVariants(productVariantsList);
    }

    private NewVariantForNewProduct toNewVariantInput(
            final NewProductVariant variant) {
        final var price = new VariantPrice(variant.price().value());
        final var traits = new VariantTraits(
                variant.traits().unwrap()
                        .stream().map(VariantTrait::new).toList());

        return new NewVariantForNewProduct(
                price,
                traits);
    }

    private ProductVariant toProductVariant(
            final VariantView variantView) {
        final var id = new ProductVariantId(variantView.id());
        final var price = new ProductVariantPrice(variantView.price());

        final var traitsList = variantView.traits()
                .stream().map(ProductVariantTrait::new).toList();
        final var traits = new ProductVariantTraits(traitsList);

        return new ProductVariant(
                id,
                price,
                traits);
    }
}
