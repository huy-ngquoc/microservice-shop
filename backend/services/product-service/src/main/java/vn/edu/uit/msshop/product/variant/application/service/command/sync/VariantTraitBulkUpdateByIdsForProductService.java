package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
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
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
class VariantTraitBulkUpdateByIdsForProductService
        implements VariantTraitBulkUpdateByIdsForProductUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
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
    public void updateTraitsByIds(
            final VariantTraitBulkUpdateByIdsForProductCommand cmd) {
        final var newTraitsById = VariantTraitBulkUpdateByIdsForProductService
                .toNewTraitsById(cmd.traitListById());

        final var variantIdSet = newTraitsById.keySet();
        final var amountVariants = variantIdSet.size();
        final var variantById = this.loadAllPort.loadAllByIds(variantIdSet);

        final var next = new ArrayList<Variant>(amountVariants);
        for (final var variant : variantById.values()) {
            final var variantId = variant.getId();
            final var newTraits = newTraitsById.get(variantId);

            final var updated = VariantTraitBulkUpdateByIdsForProductService
                    .withNewTraits(variant, newTraits);
            next.add(updated);
        }

        final var saved = this.updateAllPort.updateAll(next);
        for (final var variant : saved) {
            final var event = new VariantInfoUpdatedEvent(variant.getId());
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

    private static Variant withNewTraits(
            final Variant variant,
            @Nullable
            final VariantTraits newTraits) {
        if (newTraits == null) {
            throw new BusinessRuleException("Missing traits for variant: " + variant.getId().value());
        }

        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                newTraits,
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                variant.getDeletionTime());
    }
}
