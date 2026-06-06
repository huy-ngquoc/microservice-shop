package vn.edu.uit.msshop.product.brand.application.service.command.logo;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLogoLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.exception.BrandLogoKeyNotFoundException;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLogoLifecycleUseCases;
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
public class BrandLogoDeletionService
        implements BrandLogoLifecycleUseCases.Delete {

    private final LoadBrandPort loadPort;
    private final UpdateBrandPort updatePort;

    private final BrandLogoStoragePort logoStoragePort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND,
                            key = "#cmd.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND_LIST,
                            allEntries = true)
            })
    public void delete(
            final BrandLogoLifecycleCommands.Delete cmd) {
        final var brandId = cmd.id();
        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var oldKey = brand.getLogoKey();
        if (oldKey == null) {
            return;
        }

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var next = new Brand(
                brand.getId(),
                brand.getName(),
                null,
                expectedVersion,
                brand.getDeletionTime());
        final var saved = this.updatePort.update(next);

        final var event = new BrandLogoUpdated(
                saved.getId(),
                null,
                oldKey);
        this.eventPort.publish(event);

        this.deleteOldLogo(oldKey);
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
            log.warn("Failed to delete old image key '{}', manual cleanup required",
                    oldKey.value(),
                    e);
        }
    }
}
