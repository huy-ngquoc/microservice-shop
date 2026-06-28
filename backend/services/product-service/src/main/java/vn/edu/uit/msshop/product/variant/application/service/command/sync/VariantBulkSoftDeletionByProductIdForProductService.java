package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByProductIdForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedForProductEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByProductIdForProductService
        implements VariantBulkSoftDeletionByProductIdForProductUseCase {

    private final VariantActiveBulkLookupByProductIdPort activeBulkLookupByProductIdPort;
    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByVariantIdsPort;
    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByVariantIdsPort;
    private final VariantBulkUpdatePort bulkUpdatePort;

    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            allEntries = true),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public void softDeleteAll(
            final VariantBulkSoftDeletionByProductIdForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());

        final var activeVariants = this.activeBulkLookupByProductIdPort
                .loadAllActiveByProductId(productId);
        if (activeVariants.isEmpty()) {
            return;
        }

        final var nextList = activeVariants.stream()
                .map(Variant::softDeleted)
                .toList();
        final var savedList = this.bulkUpdatePort.updateAll(nextList);

        final var variantIdSet = savedList.stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());
        final var soldCountById = this.soldCountBulkLookupByVariantIdsPort
                .loadAllByVariantIds(variantIdSet);
        final var stockCountById = this.stockCountBulkLookupByVariantIdsPort
                .loadAllByVariantIds(variantIdSet);

        for (final var variant : savedList) {
            final var variantId = variant.getId();
            final var soldCount = soldCountById.getOrDefault(
                    variantId,
                    VariantSoldCount.zero(variantId, productId));
            final var stockCount = stockCountById.getOrDefault(
                    variantId,
                    VariantStockCount.zero(variantId, productId));

            final var event = VariantSoftDeletedForProductEvent.of(
                    variant,
                    soldCount,
                    stockCount);
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
