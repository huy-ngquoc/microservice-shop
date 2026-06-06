package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.DeleteBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.brand.application.service.command.logo.BrandLogoDeleter;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandPurged;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandHardDeletionService
        implements BrandLifecycleUseCases.HardDelete {

    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final DeleteBrandPort deletePort;

    private final BrandLogoDeleter logoDeleter;

    private final CheckBrandHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;

    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void hardDelete(
            final BrandLifecycleCommands.HardDelete cmd) {
        final var brandId = cmd.id();
        final var brand = this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        BrandVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                brand.getVersion());

        if (this.checkHasSoftDeletedProductsPort.hasSoftDeletedProduct(brandId)) {
            throw new BusinessRuleException("Cannot delete brand with existing products");
        }

        this.deletePort.deleteById(brandId);

        final var event = new BrandPurged(brandId);
        this.eventPort.publish(event);

        this.logoDeleter.deleteQuietly(brand.getLogoKey());
    }
}
