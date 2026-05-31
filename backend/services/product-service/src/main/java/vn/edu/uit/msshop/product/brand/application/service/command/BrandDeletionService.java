package vn.edu.uit.msshop.product.brand.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.DeleteBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasProductsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandPurged;
import vn.edu.uit.msshop.product.brand.domain.event.BrandRestored;
import vn.edu.uit.msshop.product.brand.domain.event.BrandSoftDeleted;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandDeletionTime;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandDeletionService
        implements
        BrandLifecycleUseCases.SoftDelete,
        BrandLifecycleUseCases.Restore,
        BrandLifecycleUseCases.HardDelete {
    private final LoadBrandPort loadPort;
    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final UpdateBrandPort updatePort;
    private final DeleteBrandPort deletePort;

    private final BrandLogoStoragePort logoStoragePort;

    private final CheckBrandHasProductsPort checkHasProductsPort;
    private final CheckBrandHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;

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
    public void softDelete(
            final BrandLifecycleCommands.SoftDelete cmd) {
        final var brandId = cmd.id();
        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        if (this.checkHasProductsPort.hasProducts(brandId)) {
            throw new BusinessRuleException("Cannot delete brand with existing products");
        }

        final var next = new Brand(
                brand.getId(),
                brand.getName(),
                brand.getLogoKey(),
                brand.getVersion(),
                BrandDeletionTime.now());

        final var saved = this.updatePort.update(next);
        this.eventPort.publish(new BrandSoftDeleted(saved.getId()));
    }

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.BRAND_LIST,
            allEntries = true)
    public void restore(
            final BrandLifecycleCommands.Restore command) {
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

    @Override
    @Transactional
    public void hardDelete(
            final BrandLifecycleCommands.HardDelete cmd) {
        final var brandId = cmd.id();
        final var brand = this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var expectedVersion = cmd.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        if (this.checkHasSoftDeletedProductsPort.hasSoftDeletedProduct(brandId)) {
            throw new BusinessRuleException("Cannot delete brand with existing products");
        }

        this.deletePort.deleteById(brandId);
        this.eventPort.publish(new BrandPurged(brandId));

        this.deleteLogo(brand.getLogoKey());
    }

    private void deleteLogo(
            @Nullable
            final BrandLogoKey key) {
        if (key == null) {
            return;
        }

        try {
            this.logoStoragePort.deleteLogo(key);
        } catch (final RuntimeException e) {
            log.warn("Hard delete: failed to delete logo '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
