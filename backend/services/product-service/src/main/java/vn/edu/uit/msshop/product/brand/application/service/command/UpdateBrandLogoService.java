package vn.edu.uit.msshop.product.brand.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandLogoKeyNotFoundException;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.UpdateBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

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
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND,
                            key = "#command.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND_LIST,
                            allEntries = true)
            })
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

        final var saved = this.commitImageChange(brand, command.newLogoKey());
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
            final BrandLogoKey newLogoKey) {
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
                    current.getVersion(),
                    current.getDeletionTime());
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
        } catch (final RuntimeException e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
