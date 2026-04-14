package vn.edu.uit.msshop.product.variant.application.service.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;
import vn.edu.uit.msshop.product.variant.application.dto.command.RestoreVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.RestoreVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadSoftDeletedVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.AddVariantToProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantRestorablePort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestored;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

@Service
@RequiredArgsConstructor
public class RestoreVariantService implements RestoreVariantUseCase {
    private final LoadSoftDeletedVariantPort loadSoftDeletedPort;
    private final CheckVariantRestorablePort checkRestorablePort;
    private final AddVariantToProductPort addToProductPort;
    private final UpdateVariantPort updatePort;
    private final PublishVariantEventPort eventPort;
    private final VariantUpdateRepository variantUpdateRepo;
    private final PublishProductEventPort publishProductEventPort;

    @Override
    @Transactional
    public void restore(
            final RestoreVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadSoftDeletedPort
                .loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(), currentVersion.value());
        }

        this.checkRestorablePort.validateRestorable(variant);

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                null);
        final var saved = this.updatePort.update(next);

        this.addToProductPort.addToProduct(variant);
        VariantUpdateDocument eventDocument = new VariantUpdateDocument(UUID.randomUUID(), saved.getId().value(), saved.getProductId().value(), "",
                 saved.getPrice().value(), getTraits(saved), saved.getImageKey().value(), "PENDING", 0, Instant.now(), null, null);
        VariantUpdateDocument savedEventDodcument = variantUpdateRepo.save(eventDocument);
        publishProductEventPort.publishVariantUpdated(savedEventDodcument);

        this.eventPort.publish(new VariantRestored(saved.getId()));
    }
    private List<String> getTraits(Variant v) {
        return v.getTraits().values().stream().map(item->item.value()).toList();
    }
}
