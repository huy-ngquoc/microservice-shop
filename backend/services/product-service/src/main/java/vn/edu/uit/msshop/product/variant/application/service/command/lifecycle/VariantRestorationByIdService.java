package vn.edu.uit.msshop.product.variant.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantRestorationByIdCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantRestorationByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantSoldCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.count.query.VariantStockCountLookupByVariantIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command.VariantUpdatePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.AddVariantToProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantRestorablePort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantVersionGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestoredEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Service
@RequiredArgsConstructor
class VariantRestorationByIdService
        implements VariantRestorationByIdUseCase {

    private final VariantSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final VariantSoldCountLookupByVariantIdPort soldCountLookupByIdPort;
    private final VariantStockCountLookupByVariantIdPort stockCountLookupByIdPort;
    private final CheckVariantRestorablePort checkRestorablePort;
    private final AddVariantToProductPort addToProductPort;
    private final VariantUpdatePort updatePort;
    private final VariantEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.VARIANT_LIST,
            allEntries = true)
    public void restore(
            final VariantRestorationByIdCommand cmd) {
        final var variantId = new VariantId(cmd.variantId());
        final var expectedVersion = new VariantVersion(cmd.variantVersion());

        final var variant = this.softDeletedLookupByIdPort
                .loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        VariantVersionGuard.ensureMatch(
                expectedVersion,
                variant.getVersion());

        this.checkRestorablePort.validateRestorable(variant);

        final var productId = variant.getProductId();
        final var soldCount = this.soldCountLookupByIdPort.loadByVariantIdOrZero(
                variantId, productId);
        final var stockCount = this.stockCountLookupByIdPort.loadByVariantIdOrZero(
                variantId, productId);
        final var soldIncrement = soldCount.getValue().value();
        final var stockIncrement = stockCount.getValue().value();

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                null);
        final var saved = this.updatePort.update(next);

        this.addToProductPort.addToProduct(
                saved,
                soldIncrement,
                stockIncrement);

        final var event = new VariantRestoredEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
