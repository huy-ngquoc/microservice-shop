package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkSoftDeletionByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantBulkSoftDeletionByIdsForProductService
        implements VariantBulkSoftDeletionByIdsForProductUseCase {

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
    public void deleteByIds(
            final VariantBulkSoftDeletionByIdsForProductCommand cmd) {
        final var variantIdSet = cmd.idSet().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        final var variantById = this.loadAllPort.loadAllByIds(variantIdSet);

        final var next = variantById.values().stream()
                .map(VariantBulkSoftDeletionByIdsForProductService::toSoftDeleted)
                .toList();
        final var saved = this.updateAllPort.updateAll(next);

        for (final var variant : saved) {
            final var event = new VariantSoftDeletedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }

    private static Variant toSoftDeleted(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());
    }
}
