package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantAdditionForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantAdditionForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVariantGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

@Service
@RequiredArgsConstructor
class ProductVariantAdditionForVariantService
        implements ProductVariantAdditionForVariantUseCase {

    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductSoldCountBulkIncrementPort soldCountBulkIncrementPort;
    private final ProductStockCountBulkIncrementPort stockCountBulkIncrementPort;

    private final ProductEventPublicationPort eventPort;

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
    public void add(
            final ProductVariantAdditionForVariantCommand cmd) {
        final var productId = new ProductId(cmd.productId());

        final var variantId = new ProductVariantId(cmd.variantId());
        final var variantPrice = new ProductVariantPrice(cmd.variantPrice());
        final var variantTraits = ProductVariantTraits.of(cmd.variantTraitList());
        final var newVariant = new ProductVariant(
                variantId,
                variantPrice,
                variantTraits);

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariantGuard.ensureNotProductSimple(product);
        ProductVariantGuard.ensureNoDuplicateCombination(
                product,
                variantTraits);

        final var next = product.addVariant(newVariant);
        final var saved = this.updatePort.update(next);

        if (cmd.productSoldCountIncrement() > 0) {
            final var incrementByProductId = Map.of(productId, cmd.productSoldCountIncrement());
            this.soldCountBulkIncrementPort.increaseAll(incrementByProductId);
        }
        if (cmd.productStockCountIncrement() > 0) {
            final var incrementByProductId = Map.of(productId, cmd.productStockCountIncrement());
            this.stockCountBulkIncrementPort.increaseAll(incrementByProductId);
        }

        final var event = new ProductInfoUpdatedEvent(saved.getId());
        this.eventPort.publishEvent(event);
    }
}
