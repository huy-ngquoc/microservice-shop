package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRemovalForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductMustHaveAtLeastOneVariantException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRemovalForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkDecreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkDecreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductInfoUpdatedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;

@Service
@RequiredArgsConstructor
class ProductVariantRemovalForVariantService
        implements ProductVariantRemovalForVariantUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductSoldCountBulkDecreationPort soldCountBulkDecreationPort;
    private final ProductStockCountBulkDecreationPort stockCountBulkDecreationPort;

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
    public void remove(
            final ProductVariantRemovalForVariantCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var variantId = new ProductVariantId(cmd.variantId());

        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getVariants().size() <= 1) {
            throw new ProductMustHaveAtLeastOneVariantException(productId);
        }

        final var currentConfig = product.getConfiguration();
        final var newConfig = currentConfig.removeVariant(variantId);

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfig,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());
        final var saved = this.updatePort.update(next);

        if (cmd.productSoldCountDecrement() > 0) {
            final var decrementByProductId = Map.of(productId, cmd.productSoldCountDecrement());
            this.soldCountBulkDecreationPort.decreaseAll(decrementByProductId);
        }
        if (cmd.productStockCountDecrement() > 0) {
            final var decrementByProductId = Map.of(productId, cmd.productStockCountDecrement());
            this.stockCountBulkDecreationPort.decreaseAll(decrementByProductId);
        }

        this.eventPort.publishEvent(new ProductInfoUpdatedEvent(saved.getId()));
    }
}
