package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantTraitBulkUpdateByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantTraitBulkUpdateByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantBulkUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantSyncGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
class VariantTraitBulkUpdateByIdsForProductService
        implements VariantTraitBulkUpdateByIdsForProductUseCase {
    private final VariantActiveBulkLookupByIdsPort activeBulkLookupByIdsPort;
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
    public void updateAll(
            final VariantTraitBulkUpdateByIdsForProductCommand cmd) {
        final var productId = new VariantProductId(cmd.productId());
        final var newTraitsById = VariantTraitBulkUpdateByIdsForProductService
                .toNewTraitsById(cmd.traitListById());

        final var variantIdSet = newTraitsById.keySet();
        final var variantById = this.activeBulkLookupByIdsPort.loadAllByIds(variantIdSet);
        VariantSyncGuard.ensureAllVariantsFound(
                variantIdSet,
                variantById);
        VariantSyncGuard.ensureAllBelongToProduct(
                variantById.values(),
                productId);

        final var amountVariants = variantById.size();
        final var next = new ArrayList<Variant>(amountVariants);
        for (final var variant : variantById.values()) {
            final var variantId = variant.getId();
            final var newTraits = newTraitsById.get(variantId);
            if (newTraits == null) {
                throw new BusinessRuleException("Missing traits for variant: " + variant.getId().value());
            }

            final var updated = variant.changeTraits(newTraits);
            next.add(updated);
        }

        final var saved = this.bulkUpdatePort.updateAll(next);
        for (final var variant : saved) {
            final var event = VariantInfoUpdatedEvent.of(variant);
            this.eventPublicationPort.publishEvent(event);
        }
    }

    private static Map<VariantId, VariantTraits> toNewTraitsById(
            final Map<UUID, List<String>> traitListById) {
        final var newTraitsByVariantId = HashMap
                .<VariantId, VariantTraits>newHashMap(traitListById.size());
        for (final var entry : traitListById.entrySet()) {
            newTraitsByVariantId.put(
                    new VariantId(entry.getKey()),
                    VariantTraits.of(entry.getValue()));
        }
        return newTraitsByVariantId;
    }
}
