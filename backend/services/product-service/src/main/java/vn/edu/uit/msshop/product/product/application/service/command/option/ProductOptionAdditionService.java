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
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantTraitBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Service
@RequiredArgsConstructor
class ProductOptionAdditionService
        implements ProductOptionAdditionUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantTraitBulkUpdatePort variantTraitBulkUpdatePort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort loadStockCountPort;
    private final ProductRatingLookupByIdPort stockCountLookupByIdPort;

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

        final var currentConfig = product.getConfiguration();
        final var newConfig = currentConfig.addOption(
                newOption,
                defaultTrait);

        final var newTraitsMap = HashMap.<ProductVariantId, ProductVariantTraits>newHashMap(
                newConfig.variants().size());
        for (final var v : newConfig.variants().values()) {
            newTraitsMap.put(v.id(), v.traits());
        }

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfig,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());

        final var savedProduct = this.updatePort.update(next);
        final var savedProductId = savedProduct.getId();

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(savedProductId);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(savedProductId);
        final var rating = this.stockCountLookupByIdPort.loadByIdOrZero(savedProductId);

        final var event = new ProductInfoUpdatedEvent(savedProductId);
        this.eventPublicationPort.publishEvent(event);

        this.variantTraitBulkUpdatePort.updateTraitsByIds(newTraitsMap);

        return this.mapper.toView(
                savedProduct,
                soldCount,
                stockCount,
                rating);
    }
}
