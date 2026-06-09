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
import vn.edu.uit.msshop.product.product.domain.event.ProductSoftDeletedEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductDeletionTime;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductSoftDeletionService
        implements
        ProductSoftDeletionUseCase {
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
                            key = "#command.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void softDelete(
            final ProductSoftDeletionCommand command) {
        final var productId = command.id();
        final var product = this.loadPort.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // TODO: duplicate code across services, move it to somewhere to reuse
        final var expectedVersion = command.expectedVersion();
        final var currentVersion = product.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

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

        this.eventPublicationPort.publishEvent(new ProductSoftDeletedEvent(saved.getId()));
    }
}
