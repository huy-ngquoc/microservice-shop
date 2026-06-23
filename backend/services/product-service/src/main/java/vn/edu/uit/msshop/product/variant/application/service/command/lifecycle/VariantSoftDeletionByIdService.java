package vn.edu.uit.msshop.product.variant.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantSoftDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantSoftDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.VariantToProductRemovalPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantVersionGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Service
@RequiredArgsConstructor
class VariantSoftDeletionByIdService
        implements VariantSoftDeletionByIdUseCase {

    private final VariantActiveLookupByIdPort activeLookupByIdPort;
    private final VariantSoldCountLookupByVariantIdPort soldCountLookupByIdPort;
    private final VariantStockCountLookupByVariantIdPort stockCountLookupByIdPort;
    private final VariantUpdatePort updatePort;
    private final VariantToProductRemovalPort removeFromProductPort;
    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            key = "#cmd.variantId()"),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public void softDelete(
            final VariantSoftDeletionByIdCommand cmd) {
        final var variantId = new VariantId(cmd.variantId());
        final var expectedVersion = new VariantVersion(cmd.variantVersion());

        final var variant = this.activeLookupByIdPort.loadActiveById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        VariantVersionGuard.ensureMatch(
                expectedVersion,
                variant.getVersion());

        final var productId = variant.getProductId();
        final var soldCount = this.soldCountLookupByIdPort.loadByVariantIdOrZero(
                variantId, productId);
        final var stockCount = this.stockCountLookupByIdPort.loadByVariantIdOrZero(
                variantId, productId);
        final var soldDecrement = soldCount.getValue().value();
        final var stockDecrement = stockCount.getValue().value();

        final var next = variant.softDeleted();
        final var saved = this.updatePort.update(next);

        this.removeFromProductPort.removeFromProduct(
                saved.getId(),
                saved.getProductId(),
                soldDecrement,
                stockDecrement);

        final var event = VariantSoftDeletedEvent.of(
                saved,
                soldCount,
                stockCount);
        this.eventPublicationPort.publishEvent(event);
    }
}
