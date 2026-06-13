package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkRestorationByIdsForProductCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkRestorationByIdsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllSoftDeletedVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestoredEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
class VariantBulkRestorationByIdsForProductService
        implements VariantBulkRestorationByIdsForProductUseCase {

    private final LoadAllSoftDeletedVariantsPort loadAllSoftDeletedPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.VARIANT_LIST,
            allEntries = true)
    public void restoreByIds(
            final VariantBulkRestorationByIdsForProductCommand cmd) {
        final var variantIdSet = cmd.idSet().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());

        final var variants = this.loadAllSoftDeletedPort
                .loadAllSoftDeletedByIds(variantIdSet);

        final var next = variants.stream()
                .map(VariantBulkRestorationByIdsForProductService::toRestored)
                .toList();
        final var saved = this.updateAllPort.updateAll(next);

        for (final var variant : saved) {
            final var event = new VariantRestoredEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }

    private static Variant toRestored(
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
                null);
    }
}
