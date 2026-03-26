package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.Collection;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteAllVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Component
@RequiredArgsConstructor
public class ProductToVariantSyncAdapter
        implements CreateAllProductVariantsPort,
        SoftDeleteAllProductVariantsPort,
        SoftDeleteVariantsForProductPort {
    private final CreateVariantsForNewProductUseCase createForNewProductUseCase;
    private final SoftDeleteAllVariantsUseCase softDeleteAllUseCase;
    private final SoftDeleteVariantsForProductUseCase softDeleteForProductUseCase;

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

        final var variantViewsList = this.createForNewProductUseCase.create(command);

        final var productVariantsList = variantViewsList.stream()
                .map(this::toProductVariant).toList();
        return new ProductVariants(productVariantsList);
    }

    @Override
    public void deleteByProductId(
            final ProductId id) {
        final var productId = new VariantProductId(id.value());
        this.softDeleteForProductUseCase.deleteByProductId(productId);
    }

    @Override
    public void deleteByIds(
            final Collection<ProductVariantId> variantIds) {
        final var ids = variantIds.stream()
                .map(ProductVariantId::value)
                .map(VariantId::new)
                .toList();
        this.softDeleteAllUseCase.deleteByIds(ids);
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
