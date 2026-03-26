package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.SoftDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantDeletionTime;

@Service
@RequiredArgsConstructor
public class SoftDeleteVariantService implements SoftDeleteVariantUseCase {
    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;
    private final RemoveVariantFromProductPort removeFromProductPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void delete(
            final SoftDeleteVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        this.removeFromProductPort.removeFromProduct(
                variantId,
                variant.getProductId());

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());

        final var saved = this.updatePort.update(next);

        this.eventPort.publish(new VariantSoftDeleted(saved.getId()));
    }
}
