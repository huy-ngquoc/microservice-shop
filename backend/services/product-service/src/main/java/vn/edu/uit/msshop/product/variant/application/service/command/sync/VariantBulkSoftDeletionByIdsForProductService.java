package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantSyncGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedForProductEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByIdsForProductService
        implements VariantBulkSoftDeletionByIdsForProductUseCase {

    private final VariantActiveBulkLookupByIdsPort activeBulkLookupByIdsPort;
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
            final VariantBulkSoftDeletionByIdsForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());
        final var variantIdSet = cmd.variantIdSet().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        final var variantById = this.activeBulkLookupByIdsPort.loadAllByIds(variantIdSet);
        VariantSyncGuard.ensureAllVariantsFound(variantIdSet, variantById);
        VariantSyncGuard.ensureAllBelongToProduct(
                variantById.values(),
                productId);

        final var nextList = variantById.values().stream()
                .map(Variant::softDeleted)
                .toList();
        final var savedList = this.bulkUpdatePort.updateAll(nextList);

        for (final var variant : savedList) {
            final var event = VariantSoftDeletedForProductEvent.of(variant);
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
