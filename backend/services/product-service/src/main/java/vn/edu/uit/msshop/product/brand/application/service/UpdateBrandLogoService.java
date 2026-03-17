package vn.edu.uit.msshop.product.brand.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandLogoKeyNotFoundException;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.UpdateBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateBrandLogoService implements UpdateBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final UpdateBrandPort updatePort;
    private final BrandLogoStoragePort logoStoragePort;
    private final BrandViewMapper mapper;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public BrandLogoView updateLogo(
            UpdateBrandLogoCommand command) {
        final var brandId = command.id();
        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var saved = this.commitImageChange(brand, command.newLogoKey(), expectedVersion);
        if (saved == null) {
            return this.mapper.toLogoView(brand);
        }

        final var event = new BrandLogoUpdated(
                saved.getId(),
                saved.getLogoKey(),
                brand.getLogoKey());
        this.eventPort.publish(event);

        this.deleteOldLogo(brand.getLogoKey());

        return this.mapper.toLogoView(saved);
    }

    private @Nullable Brand commitImageChange(
            final Brand current,
            final BrandLogoKey newLogoKey,
            final BrandVersion expectedVersion) {
        if (newLogoKey.equals(current.getLogoKey())) {
            return null;
        }

        this.ensureLogoKeyExistsInTemp(newLogoKey);
        this.logoStoragePort.publishLogo(newLogoKey);

        try {
            final var next = new Brand(
                    current.getId(),
                    current.getName(),
                    newLogoKey,
                    expectedVersion);
            return this.updatePort.update(next);
        } catch (final RuntimeException e) {
            try {
                this.logoStoragePort.unpublishLogo(newLogoKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", newLogoKey.value(), compensateEx);
            }
            throw e;
        }
    }

    private void ensureLogoKeyExistsInTemp(
            final BrandLogoKey logoKey) {
        if (!this.logoStoragePort.existsAsTemp(logoKey)) {
            throw new BrandLogoKeyNotFoundException(logoKey);
        }
    }

    private void deleteOldLogo(
            @Nullable
            final BrandLogoKey oldKey) {
        if (oldKey == null) {
            return;
        }

        try {
            this.logoStoragePort.deleteLogo(oldKey);
        } catch (final Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
