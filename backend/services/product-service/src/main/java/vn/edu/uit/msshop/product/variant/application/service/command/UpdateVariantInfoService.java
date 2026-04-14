package vn.edu.uit.msshop.product.variant.application.service.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantInfoUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.UpdateVariantInProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
public class UpdateVariantInfoService implements UpdateVariantInfoUseCase {
    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;
    private final UpdateVariantInProductPort updateInProductPort;
    private final PublishVariantEventPort eventPort;
    private final VariantViewMapper mapper;
    private final VariantUpdateRepository variantUpdateRepo;
    private final PublishProductEventPort publishProductEventPort;

    @Override
    @Transactional
    // TODO: traits size must be the same
    public VariantView updateInfo(
            final UpdateVariantInfoCommand command) {
        final var variant = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new VariantNotFoundException(command.id()));

        final var priceSet = command.price().getSet();
        final var traitsSet = command.traits().getSet();
        final var targetsSet = command.targets().getSet();

        if ((priceSet == null)
                && (traitsSet == null)
                && (targetsSet == null)) {
            return this.mapper.toView(variant);
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = UpdateVariantInfoService.applyChanges(
                variant,
                priceSet,
                traitsSet,
                targetsSet);
        if (next == null) {
            return this.mapper.toView(variant);
        }

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new VariantUpdated(saved.getId()));

        VariantUpdateDocument eventDocument = new VariantUpdateDocument(UUID.randomUUID(), saved.getId().value(), saved.getProductId().value(), "",
                 saved.getPrice().value(), getTraits(saved), saved.getImageKey()==null?"":saved.getImageKey().value(), "PENDING", 0, Instant.now(), null, null);

        this.updateInProductPort.updateInProduct(saved);
        publishProductEventPort.publishVariantUpdated(variantUpdateRepo.save(eventDocument));

        return this.mapper.toView(saved);
    }
    private List<String> getTraits(Variant v) {
        return v.getTraits().values().stream().map(item->item.value()).toList();
    }

    private static @Nullable Variant applyChanges(
            final Variant current,
            final Change.@Nullable Set<VariantPrice> priceSet,
            final Change.@Nullable Set<VariantTraits> traitsSet,
            final Change.@Nullable Set<VariantTargets> targetsSet) {
        final VariantPrice newPrice;
        final boolean priceUnchanged;
        if ((priceSet != null) && !priceSet.value().equals(current.getPrice())) {
            newPrice = priceSet.value();
            priceUnchanged = false;
        } else {
            newPrice = current.getPrice();
            priceUnchanged = true;
        }

        final VariantTraits newTraits;
        final boolean traitsUnchanged;
        if ((traitsSet != null) && !traitsSet.value().equals(current.getTraits())) {
            newTraits = traitsSet.value();
            traitsUnchanged = false;
        } else {
            newTraits = current.getTraits();
            traitsUnchanged = true;
        }

        final VariantTargets newTargets;
        final boolean targetsUnchanged;
        if ((targetsSet != null) && !targetsSet.value().equals(current.getTargets())) {
            newTargets = targetsSet.value();
            targetsUnchanged = false;
        } else {
            newTargets = current.getTargets();
            targetsUnchanged = true;
        }

        if (priceUnchanged && traitsUnchanged && targetsUnchanged) {
            return null;
        }

        return new Variant(
                current.getId(),
                current.getProductId(),
                newPrice,
                current.getSoldCount(),
                newTraits,
                newTargets,
                current.getImageKey(),
                current.getVersion(),
                current.getDeletionTime());
    }
}
