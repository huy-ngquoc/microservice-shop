package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSoftDeletionCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductSoftDeletionUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkSoftDeletionForProductPort;
import vn.edu.uit.msshop.product.product.application.service.command.support.ProductVersionGuard;
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeletedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

@Service
@RequiredArgsConstructor
public class ProductSoftDeletionService
        implements ProductSoftDeletionUseCase {
    private final ProductActiveLookupByIdPort loadPort;
    private final ProductUpdatePort updatePort;

    private final ProductVariantBulkSoftDeletionForProductPort variantBulkSoftDeletionForProductPort;

    private final ProductEventPublicationPort eventPublicationPort;

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
    public void softDelete(
            final ProductSoftDeletionCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var expectedVersion = new ProductVersion(cmd.productVersion());

        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVersionGuard.ensureMatch(
                expectedVersion,
                product.getVersion());

        final var next = new Product(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getBrandId(),
                product.getConfiguration(),
                product.getImageKeys(),
                product.getVersion(),
                ProductDeletionTime.now());
        final var saved = this.updatePort.update(next);

        this.variantBulkSoftDeletionForProductPort.deleteByProductId(saved.getId());

        final var event = new ProductSoftDeletedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
