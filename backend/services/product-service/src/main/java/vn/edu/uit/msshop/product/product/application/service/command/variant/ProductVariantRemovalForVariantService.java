package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.RemoveProductVariantForVariantCommand;
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

@Service
@RequiredArgsConstructor
public class ProductVariantRemovalForVariantService
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
                            key = "#command.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void remove(
            RemoveProductVariantForVariantCommand command) {
        final var productId = command.id();
        final var product = this.activeLookupByIdPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getVariants().size() <= 1) {
            throw new ProductMustHaveAtLeastOneVariantException(productId);
        }

        final var newConfiguration = product.getConfiguration()
                .removeVariant(command.variantId());

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                newConfiguration,
                product.getImageKeys(),
                product.getVersion(),
                product.getDeletionTime());
        final var saved = this.updatePort.update(next);

        if (command.soldDecrement() > 0) {
            this.soldCountBulkDecreationPort.decreaseAll(Map.of(productId, command.soldDecrement()));
        }
        if (command.stockDecrement() > 0) {
            this.stockCountBulkDecreationPort.decreaseAll(Map.of(productId, command.stockDecrement()));
        }

        this.eventPort.publishEvent(new ProductInfoUpdatedEvent(saved.getId()));
    }
}
