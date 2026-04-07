package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadSoftDeletedVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantPurged;

@Service
@RequiredArgsConstructor
public class HardDeleteVariantService implements HardDeleteVariantUseCase {
    private final LoadSoftDeletedVariantPort loadSoftDeletedPort;
    private final CheckProductExistsUseCase checkProductExistsUseCase;
    private final DeleteVariantPort deletePort;
    private final VariantImageStoragePort imageStoragePort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void purge(
            final HardDeleteVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadSoftDeletedPort
                .loadSoftDeletedById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        // TODO: do we need to check variant in product deleted first?

        this.deletePort.deleteById(variantId);
        this.eventPort.publish(new VariantPurged(variantId));

        this.imageStoragePort.deleteImage(variant.getImageKey());
    }

}
