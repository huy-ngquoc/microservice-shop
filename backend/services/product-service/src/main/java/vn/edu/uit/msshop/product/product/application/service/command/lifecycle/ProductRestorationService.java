package vn.edu.uit.msshop.product.product.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductRestorationCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductRestorationUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.ProductEventPublicationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkRestorationByIdsPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductRestoredEvent;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductRestorationService
        implements
        ProductRestorationUseCase {
    private final ProductSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final ProductUpdatePort updatePort;
    private final ProductVariantBulkRestorationByIdsPort variantBulkRestorationForProductPort;

    private final ProductEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.PRODUCT_LIST,
            allEntries = true)
    public void restore(
            final ProductRestorationCommand command) {
        final var productId = command.id();
        final var product = this.softDeletedLookupByIdPort
                .loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

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
                null);
        final var saved = this.updatePort.update(next);

        final var manifestIds = saved.getVariants().values().stream()
                .map(ProductVariant::id)
                .toList();
        this.variantBulkRestorationForProductPort.restoreByVariantIds(manifestIds);

        this.eventPublicationPort.publishEvent(new ProductRestoredEvent(saved.getId()));
    }
}
