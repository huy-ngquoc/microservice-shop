package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountBulkLookupByVariantIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantSyncGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestoredForProductEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class VariantBulkRestorationByIdsForProductService
        implements VariantBulkRestorationByIdsForProductUseCase {

    private final VariantSoftDeletedBulkLookupByIdsPort softDeletedBulkLookupByIdsPort;
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
    @CacheEvict(
            cacheNames = CacheNames.VARIANT_LIST,
            allEntries = true)
    public void restoreAll(
            final VariantBulkRestorationByIdsForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());
        final var variantIdSet = cmd.variantIdSet().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        final var variantById = this.softDeletedBulkLookupByIdsPort
                .loadAllSoftDeletedByIds(variantIdSet);
        VariantSyncGuard.ensureAllVariantsFound(
                variantIdSet,
                variantById);
        VariantSyncGuard.ensureAllBelongToProduct(
                variantById.values(),
                productId);

        final var next = variantById.values().stream()
                .map(Variant::restored)
                .toList();
        final var saved = this.bulkUpdatePort.updateAll(next);

        final var soldCountById = this.soldCountBulkLookupByVariantIdsPort
                .loadAllByVariantIds(variantIdSet);
        final var stockCountById = this.stockCountBulkLookupByVariantIdsPort
                .loadAllByVariantIds(variantIdSet);

        for (final var variant : saved) {
            final var variantId = variant.getId();
            final var soldCount = soldCountById.getOrDefault(
                    variantId,
                    VariantSoldCount.zero(variantId, productId));
            final var stockCount = stockCountById.getOrDefault(
                    variantId,
                    VariantStockCount.zero(variantId, productId));

            final var event = VariantRestoredForProductEvent.of(
                    variant,
                    soldCount,
                    stockCount);
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
