package vn.edu.uit.msshop.product.product.application.service.command.option;

import java.util.HashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.option.ProductOptionAdditionCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionAdditionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
class ProductOptionAdditionService
        implements ProductOptionAdditionUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantTraitBulkUpdatePort variantTraitBulkUpdatePort;
    private final ProductSoldCountLookupByProductIdPort soldCountLookupByProductIdPort;
    private final ProductStockCountLookupByProductIdPort stockCountLookupByProductIdPort;
    private final ProductRatingLookupByProductIdPort ratingLookupByProductIdPort;

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
    public ProductView add(
            final ProductOptionAdditionCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var newOption = new ProductOption(cmd.newOption());
        final var defaultTrait = new ProductVariantTrait(cmd.defaultTrait());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        ProductOptionAdditionService.ensureOptionAddable(
                product,
                newOption,
                defaultTrait);

        final var next = product.addOption(
                newOption,
                defaultTrait);
        final var newVariants = next.getVariants();

        final var newTraitsMap = HashMap.<ProductVariantId, ProductVariantTraits>newHashMap(
                newVariants.size());
        for (final var variant : newVariants.values()) {
            final var variantId = variant.id();
            final var variantTraits = variant.traits();

            newTraitsMap.put(variantId, variantTraits);
        }

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.soldCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var stockCount = this.stockCountLookupByProductIdPort.loadByProductIdOrZero(savedProductId);
        final var rating = this.ratingLookupByProductIdPort.loadByProductIdOrZero(savedProductId);

        final var event = new ProductInfoUpdatedEvent(savedProductId);
        this.eventPublicationPort.publishEvent(event);

        this.variantTraitBulkUpdatePort.updateTraitsByIds(
                newTraitsMap,
                savedProductId);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }

    private static void ensureOptionAddable(
            final Product product,
            final ProductOption newOption,
            final ProductVariantTrait defaultTrait) {
        if (product.getOptions().isFull()) {
            throw new BusinessRuleException(
                    "Product already has the maximum number of options (" + ProductOptions.MAX_AMOUNT + ")");
        }
        if (product.getOptions().containsIgnoreCase(newOption)) {
            throw new BusinessRuleException("Option already exists: " + newOption.value());
        }
        if (product.getVariants().appendTraitCollides(defaultTrait)) {
            throw new BusinessRuleException(
                    "Default trait '" + defaultTrait.value()
                            + "' collides with an existing trait value in a variant");
        }
    }
}
