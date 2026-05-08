package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.HardDeleteAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.RestoreVariantsForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.UpdateAllProductVariantTraitsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.UpdateProductNameOnVariantsPort;
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
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantProductNameForProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.RestoreVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteAllVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateAllVariantTraitsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantProductNameForProductUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Component
@RequiredArgsConstructor
public class ProductToVariantSyncAdapter
        implements
        CreateAllProductVariantsPort,
        RestoreVariantsForProductPort,
        UpdateAllProductVariantTraitsPort,
        UpdateProductNameOnVariantsPort,
        SoftDeleteAllProductVariantsPort,
        SoftDeleteVariantsForProductPort,
        HardDeleteAllProductVariantsPort {
    private final CreateVariantsForNewProductUseCase createForNewProductUseCase;
    private final RestoreVariantsForProductUseCase restoreVariantsForProductUseCase;
    private final UpdateAllVariantTraitsForProductUseCase updateAllTraitsForProductUseCase;
    private final UpdateVariantProductNameForProductUseCase updateVariantProductNameForProductUseCase;
    private final SoftDeleteAllVariantsUseCase softDeleteAllUseCase;
    private final SoftDeleteVariantsForProductUseCase softDeleteForProductUseCase;
    private final HardDeleteVariantsForProductUseCase hardDeleteVariantsForProductUseCase;

    @Override
    public ProductVariants create(
            final ProductId id,
            final ProductName name,
            final NewProductVariants newVariants) {
        final var variantProductId = new VariantProductId(id.value());
        final var variantProductName = new VariantProductName(name.value());
        final var newVariantInputsList = newVariants.values().stream()
                .map(ProductToVariantSyncAdapter::toNewVariantInput)
                .toList();
        final var newVariantInputs = new NewVariantsForNewProduct(newVariantInputsList);

        final var command = new CreateVariantsForNewProductCommand(
                variantProductId,
                variantProductName,
                newVariantInputs);

        final var variantViewsList = this.createForNewProductUseCase.create(command);

        final var productVariantsList = variantViewsList.stream()
                .map(ProductToVariantSyncAdapter::toProductVariant)
                .toList();
        return new ProductVariants(productVariantsList);
    }

    @Override
    public void restoreByVariantIds(
            final Collection<ProductVariantId> variantIds) {
        final var ids = variantIds.stream()
                .map(ProductVariantId::value)
                .map(VariantId::new)
                .toList();

        this.restoreVariantsForProductUseCase.restoreByIds(ids);
    }

    @Override
    public void updateTraitsByIds(
            Map<ProductVariantId, ProductVariantTraits> newTraitsMap) {
        final var map = new HashMap<VariantId, VariantTraits>(newTraitsMap.size(), 1);

        for (final var entry : newTraitsMap.entrySet()) {
            final var productVariantId = entry.getKey();
            final var productVariantTraits = entry.getValue();

            final var variantId = new VariantId(productVariantId.value());
            final var variantTraits = VariantTraits.of(productVariantTraits.unwrap());

            map.put(variantId, variantTraits);
        }

        this.updateAllTraitsForProductUseCase.updateTraitsByIds(map);
    }

    @Override
    public void updateProductNameByProductId(
            final ProductId id,
            final ProductName name) {
        final var variantProductId = new VariantProductId(id.value());
        final var variantProductName = new VariantProductName(name.value());

        final var command = new UpdateVariantProductNameForProductCommand(
                variantProductId,
                variantProductName);
        this.updateVariantProductNameForProductUseCase.execute(command);
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
                .collect(Collectors.toUnmodifiableSet());
        this.softDeleteAllUseCase.deleteByIds(ids);
    }

    @Override
    public void purgeByProductId(
            final ProductId productId) {
        final var variantProductId = new VariantProductId(productId.value());
        this.hardDeleteVariantsForProductUseCase.purgeByProductId(variantProductId);
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
