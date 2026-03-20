package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.out.CreateProductVariantsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateAllVariantsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateAllVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

@Component
@RequiredArgsConstructor
public class ProductVariantPersistenceAdapter
        implements CreateProductVariantsPort {
    private final CreateAllVariantsForNewProductUseCase createAllForNewProductUseCase;

    @Override
    public ProductVariants create(
            final ProductId id,
            final List<CreateProductVariantCommand> variants) {
        final var variantProductId = new VariantProductId(id.value());
        final var variantCommandsList = variants.stream()
                .map(this::toVariantCommand).toList();

        final var command = new CreateAllVariantsCommand(
                variantProductId,
                variantCommandsList);

        final var variantViewsList = this.createAllForNewProductUseCase.createAllForNewProduct(command);

        final var productVariantsList = variantViewsList.stream()
                .map(this::toProductVariant).toList();
        return new ProductVariants(productVariantsList);
    }

    private CreateAllVariantsCommand.VariantCommand toVariantCommand(
            final CreateProductVariantCommand variant) {
        final var price = new VariantPrice(variant.price().value());
        final var traits = new VariantTraits(
                variant.traits().unwrap()
                        .stream().map(VariantTrait::new).toList());

        return new CreateAllVariantsCommand.VariantCommand(
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
