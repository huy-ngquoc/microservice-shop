package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.HashMap;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.CreateAllProductVariantsPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.SoftDeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.UpdateAllProductVariantTraitsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class RemoveProductOptionService implements RemoveProductOptionUseCase {
    private final LoadProductPort loadPort;
    private final UpdateProductPort updatePort;
    private final SoftDeleteVariantsForProductPort softDeleteVariantsForProductPort;
    private final CreateAllProductVariantsPort createVariantsPort;
    private final UpdateAllProductVariantTraitsPort updateAllVariantTraitsPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final PublishProductEventPort eventPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    public ProductView removeOption(
            final RemoveProductOptionCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var expectedVersion = command.expectedVersion();
        if (!expectedVersion.equals(product.getVersion())) {
            throw new OptimisticLockException(
                    expectedVersion.value(), product.getVersion().value());
        }

        final var newConfiguration = this.removeOptionFromConfiguration(
                product, command.optionIndex(), command.defaultPrice());
        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(savedProductId);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(savedProductId);
        final var rating = this.loadRatingPort.loadByIdOrZero(savedProductId);

        this.eventPort.publish(new ProductUpdated(savedProductId));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private ProductConfiguration removeOptionFromConfiguration(
            final Product product,
            final int optionIndex,
            @Nullable final ProductPrice defaultPrice) {
        final var optionsSize = product.getOptions().size();

        if (optionsSize <= 0) {
            throw new BusinessRuleException("Product has no options to remove");
        }

        if (optionsSize > 1) {
            final var newConfig = product.getConfiguration().removeOptionAt(optionIndex);

            final var newTraitsMap = new HashMap<ProductVariantId, ProductVariantTraits>(
                    newConfig.variants().size(), 1);
            for (final var variant : newConfig.variants().values()) {
                newTraitsMap.put(variant.id(), variant.traits());
            }
            this.updateAllVariantTraitsPort.updateTraitsByIds(newTraitsMap);

            return newConfig;
        }

        if (defaultPrice == null) {
            throw new BusinessRuleException(
                    "Default price is required when removing the last option");
        }

        final var productId = product.getId();
        this.softDeleteVariantsForProductPort.deleteByProductId(productId);

        final var newVariant = new NewProductVariant(
                new ProductVariantPrice(defaultPrice.value()),
                ProductVariantTraits.empty(),
                ProductVariantTargets.empty());
        final var newVariants = this.createVariantsPort.create(
                productId,
                product.getName(),
                new NewProductVariants(List.of(newVariant)));

        return new ProductConfiguration(ProductOptions.empty(), newVariants);
    }
}
