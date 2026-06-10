package vn.edu.uit.msshop.product.product.application.service.command.option;

import java.util.HashMap;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionRemovalCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionRemovalUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkCreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkSoftDeletionForProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
class ProductOptionRemovalService
        implements ProductOptionRemovalUseCase {
    private final ProductActiveLookupByIdPort activeLookupById;
    private final ProductUpdatePort updatePort;
    private final ProductVariantBulkSoftDeletionForProductPort variantBulkSoftDeletionForProductPort;
    private final ProductVariantBulkCreationPort variantBulkCreationPort;
    private final ProductVariantTraitBulkUpdatePort variantTraitBulkUpdatePort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort stockCountLookupByIdPort;
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;

    private final ProductEventPublicationPort eventPublicationPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#cmd.productId()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public ProductView remove(
            final ProductOptionRemovalCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var defaultPrice = ProductPrice.ofNullable(cmd.defaultPrice());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupById.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        final var newConfiguration = this.removeOptionFromConfiguration(
                product,
                cmd.optionIndex(),
                defaultPrice);
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

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(savedProductId);
        final var rating = this.ratingLookupByIdPort.loadByIdOrZero(savedProductId);

        this.eventPublicationPort.publishEvent(new ProductInfoUpdatedEvent(savedProductId));

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private ProductConfiguration removeOptionFromConfiguration(
            final Product product,
            final int optionIndex,
            @Nullable
            final ProductPrice defaultPrice) {
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
            this.variantTraitBulkUpdatePort.updateTraitsByIds(newTraitsMap);

            return newConfig;
        }

        if (defaultPrice == null) {
            throw new BusinessRuleException("Default price is required when removing the last option");
        }

        final var productId = product.getId();
        this.variantBulkSoftDeletionForProductPort.deleteByProductId(productId);

        final var newVariant = new NewProductVariant(
                new ProductVariantPrice(defaultPrice.value()),
                ProductVariantTraits.empty(),
                ProductVariantTargets.empty());
        final var newVariants = this.variantBulkCreationPort.create(
                productId,
                product.getName(),
                new NewProductVariants(List.of(newVariant)));

        return new ProductConfiguration(
                ProductOptions.empty(),
                newVariants);
    }
}
