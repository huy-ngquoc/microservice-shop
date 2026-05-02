package vn.edu.uit.msshop.product.variant.application.service.command;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.repository.VariantDeletedRepository;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;

@Service
@RequiredArgsConstructor
public class SoftDeleteVariantService implements SoftDeleteVariantUseCase {
    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;
    private final RemoveVariantFromProductPort removeFromProductPort;
    private final PublishVariantEventPort eventPort;
    private final VariantDeletedRepository variantDeletedRepo;
    private final PublishProductEventPort publishProductEventPort;

    // TODO: adjust product sold count and stock count for variant.
    @Override
    @Transactional
    public void delete(
            final SoftDeleteVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));
        System.out.println("Deleteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());

        final var saved = this.updatePort.update(next);

        this.removeFromProductPort.removeFromProduct(
                saved.getId(),
                saved.getProductId());
        VariantDeletedDocument eventDocument = new VariantDeletedDocument(UUID.randomUUID(), saved.getId().value(), "PENDING", 0, Instant.now(), null, null);
        publishProductEventPort.publishVariantDeleted(variantDeletedRepo.save(eventDocument));

        this.eventPort.publish(new VariantSoftDeleted(saved.getId()));
    }
}
