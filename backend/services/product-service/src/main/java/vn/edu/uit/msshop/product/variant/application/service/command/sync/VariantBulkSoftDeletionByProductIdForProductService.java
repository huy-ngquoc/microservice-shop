package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: should delete image?
@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByProductIdForProductService
        implements VariantBulkSoftDeletionByProductIdForProductUseCase {

    private final VariantBulkLookupByProductIdPort bulkLookupByProductIdPort;
    private final VariantSoldCountBulkLookupByVariantIdsPort soldCountBulkLookupByVariantIdsPort;
    private final VariantStockCountBulkLookupByVariantIdsPort stockCountBulkLookupByVariantIdsPort;
    private final VariantBulkUpdatePort bulkUpdatePort;
    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
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

        final var activeVariants = this.bulkLookupByProductIdPort
                .loadAllActiveByProductId(productId);
        if (activeVariants.isEmpty()) {
            return;
        }

        final var next = activeVariants.stream()
                .map(Variant::softDeleted)
                .toList();

        final var saved = this.bulkUpdatePort.updateAll(next);

        final var variantIdSet = saved.stream()
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());
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

            final var event = VariantSoftDeletedEvent.of(
                    variant,
                    soldCount,
                    stockCount);
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
