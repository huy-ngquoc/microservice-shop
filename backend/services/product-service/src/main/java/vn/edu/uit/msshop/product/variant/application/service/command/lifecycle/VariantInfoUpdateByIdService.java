package vn.edu.uit.msshop.product.variant.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.Change;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantInfoUpdateByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantStockCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.UpdateVariantInProductPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantVersionGuard;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

@Service
@RequiredArgsConstructor
class VariantInfoUpdateByIdService
        implements VariantInfoUpdateByIdUseCase {

    private final LoadVariantPort loadPort;
    private final LoadVariantSoldCountPort loadSoldCountPort;
    private final LoadVariantStockCountPort loadStockCountPort;
    private final UpdateVariantPort updatePort;
    private final UpdateVariantInProductPort updateInProductPort;
    private final VariantEventPublicationPort eventPublicationPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            key = "#command.id().value()",
                            condition = "#command.price().getSet() != null || " +
                                    "#command.traits().getSet() != null || " +
                                    "#command.targets().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true,
                            condition = "#command.price().getSet() != null || " +
                                    "#command.traits().getSet() != null || " +
                                    "#command.targets().getSet() != null")
            })
    // TODO: traits size must be the same
    public VariantView updateInfo(
            final VariantInfoUpdateByIdCommand cmd) {
        final var variantId = new VariantId(cmd.variantId());
        final var priceChange = cmd.priceChange().map(VariantPrice::new);
        final var traitListChange = cmd.traitListChange().map(VariantTraits::of);
        final var targetListChange = cmd.targetListChange().map(VariantTargets::of);
        final var expectedVersion = new VariantVersion(cmd.variantVersion());

        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(
                variantId, variant.getProductId());
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(
                variantId, variant.getProductId());

        final var priceSet = priceChange.getSet();
        final var traitListSet = traitListChange.getSet();
        final var targetListSet = targetListChange.getSet();

        if ((priceSet == null)
                && (traitListSet == null)
                && (targetListSet == null)) {
            return this.mapper.toView(
                    variant,
                    soldCount,
                    stockCount);
        }

        VariantVersionGuard.ensureMatch(
                expectedVersion,
                variant.getVersion());

        final var next = VariantInfoUpdateByIdService.applyChanges(
                variant,
                priceSet,
                traitListSet,
                targetListSet);
        if (next == null) {
            return this.mapper.toView(
                    variant,
                    soldCount,
                    stockCount);
        }

        final var saved = this.updatePort.update(next);

        final var event = new VariantInfoUpdatedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);

        this.updateInProductPort.updateInProduct(saved);

        return this.mapper.toView(
                saved,
                soldCount,
                stockCount);
    }

    private static @Nullable Variant applyChanges(
            final Variant current,
            final Change.@Nullable Set<VariantPrice> priceSet,
            final Change.@Nullable Set<VariantTraits> traitsSet,
            final Change.@Nullable Set<VariantTargets> targetsSet) {
        final var applyPriceResult = Change.Set.applyChange(
                priceSet,
                current.getPrice());
        final var applyTraitsResult = Change.Set.applyChange(
                traitsSet,
                current.getTraits());
        final var applyTargetsResult = Change.Set.applyChange(
                targetsSet,
                current.getTargets());

        if (!applyPriceResult.changed()
                && !applyTraitsResult.changed()
                && !applyTargetsResult.changed()) {
            return null;
        }

        return new Variant(
                current.getId(),
                current.getProductId(),
                current.getProductName(),
                applyPriceResult.newValue(),
                applyTraitsResult.newValue(),
                applyTargetsResult.newValue(),
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }
}
