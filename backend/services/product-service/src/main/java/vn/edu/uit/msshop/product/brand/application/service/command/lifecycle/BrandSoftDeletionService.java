package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasProductsPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandSoftDeleted;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
public class BrandSoftDeletionService
        implements BrandLifecycleUseCases.SoftDelete {

    private final LoadBrandPort loadPort;
    private final UpdateBrandPort updatePort;

    private final CheckBrandHasProductsPort checkHasProductsPort;

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
    public void softDelete(
            final BrandLifecycleCommands.SoftDelete cmd) {
        final var brandId = cmd.id();
        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        BrandVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                brand.getVersion());

        if (this.checkHasProductsPort.hasProducts(brandId)) {
            throw new BusinessRuleException("Cannot delete brand with existing products");
        }

        final var next = brand.softDeleted();
        final var saved = this.updatePort.update(next);

        final var event = new BrandSoftDeleted(saved.getId());
        this.eventPort.publish(event);
    }
}
