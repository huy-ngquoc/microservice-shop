package vn.edu.uit.msshop.product.brand.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.RestoreBrandCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.RestoreBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandRestored;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class RestoreBrandService implements RestoreBrandUseCase {
    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final UpdateBrandPort updatePort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void restore(
            final RestoreBrandCommand command) {
        final var brandId = command.id();
        final var brand = this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var restored = new Brand(
                brand.getId(),
                brand.getName(),
                brand.getLogoKey(),
                brand.getVersion(),
                null);

        final var saved = this.updatePort.update(restored);
        this.eventPort.publish(new BrandRestored(saved.getId()));
    }

}
