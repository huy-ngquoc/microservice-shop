package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.CreateVariantsForNewProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.SoftDeleteVariantPort;
import vn.edu.uit.msshop.product.product.application.port.out.SoftDeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.UpdateVariantInfoPort;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantVersion;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.SoftDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.SoftDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.UpdateVariantInfoUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariantsForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTrait;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

@Component
@RequiredArgsConstructor
public class VariantPersistenceAdapter
        implements CreateVariantsForNewProductPort,
        UpdateVariantInfoPort,
        SoftDeleteVariantPort,
        SoftDeleteVariantsForProductPort {
    private final CreateVariantsForNewProductUseCase createForNewProductUseCase;
    private final UpdateVariantInfoUseCase updateInfoUseCase;
    private final SoftDeleteVariantUseCase softDeleteUseCase;
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
    public ProductVariant updateInfo(
            final ProductVariant variant) {
        final var id = new VariantId(variant.id().value());
        final var price = new VariantPrice(variant.price().value());

        final var traitsList = variant.traits().unwrap().stream()
                .map(VariantTrait::new)
                .toList();
        final var traits = new VariantTraits(traitsList);

        final var version = new VariantVersion(variant.version().value());

        final var command = new UpdateVariantInfoCommand(
                id,
                Change.set(price),
                Change.set(traits),
                version);

        final var view = this.updateInfoUseCase.updateInfo(command);

        return this.toProductVariant(view);
    }

    @Override
    public void deleteByProductId(
            final ProductId id) {
        final var productId = new VariantProductId(id.value());
        this.softDeleteForProductUseCase.deleteByProductId(productId);
    }

    @Override
    public void deleteById(
            final ProductVariantId variantId,
            final ProductVariantVersion expectedVersion) {
        final var id = new VariantId(variantId.value());
        final var version = new VariantVersion(expectedVersion.value());

        final var command = new SoftDeleteVariantCommand(
                id,
                version);

        this.softDeleteUseCase.delete(command);
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

        final var version = new ProductVariantVersion(variantView.version());

        return new ProductVariant(
                id,
                price,
                traits,
                version);
    }
}
