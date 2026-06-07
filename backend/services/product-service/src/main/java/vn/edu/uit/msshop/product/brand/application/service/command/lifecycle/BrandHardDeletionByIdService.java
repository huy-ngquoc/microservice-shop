package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandHardDeletionByIdCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandDeletionByIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.brand.application.service.command.logo.BrandLogoDeleter;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandPurged;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandHardDeletionByIdService
        implements BrandHardDeletionByIdUseCase {

    private final BrandSoftDeletedLookupByIdPort loadSoftDeletedPort;
    private final BrandDeletionByIdPort deletePort;

    private final BrandLogoDeleter logoDeleter;

    private final CheckBrandHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;

    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void hardDeleteById(
            final BrandHardDeletionByIdCommand cmd) {
        final var brandId = new BrandId(cmd.brandId());
        final var expectedVersion = new BrandVersion(cmd.brandVersion());

        final var brand = this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        BrandVersionGuard.ensureMatch(
                expectedVersion,
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
