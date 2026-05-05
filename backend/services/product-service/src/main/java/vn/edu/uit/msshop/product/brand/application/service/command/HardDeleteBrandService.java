package vn.edu.uit.msshop.product.brand.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.HardDeleteBrandCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.HardDeleteBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.DeleteBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandPurged;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardDeleteBrandService implements HardDeleteBrandUseCase {
    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final DeleteBrandPort deletePort;
    private final BrandLogoStoragePort logoStoragePort;
    private final CheckBrandHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void purge(
            final HardDeleteBrandCommand command) {
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

        if (this.checkHasSoftDeletedProductsPort.hasSoftDeletedProduct(brandId)) {
            throw new BusinessRuleException(
                    "Cannot delete brand with existing products");
        }

        this.deletePort.deleteById(brandId);
        this.eventPort.publish(new BrandPurged(brandId));

        this.deleteLogo(brand.getLogoKey());
    }

    private void deleteLogo(
            @Nullable final BrandLogoKey key) {
        if (key == null) {
            return;
        }

        try {
            this.logoStoragePort.deleteLogo(key);
        } catch (final RuntimeException e) {
            log.warn("Hard delete: failed to delete logo '{}', manual cleanup required", key.value(), e);
        }
    }
}
