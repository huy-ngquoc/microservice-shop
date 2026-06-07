package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationByIdCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandRestorationUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandRestored;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

@Service
@RequiredArgsConstructor
public class BrandRestorationService
        implements BrandRestorationUseCase {

    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final UpdateBrandPort updatePort;

    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.BRAND_LIST,
            allEntries = true)
    public void restore(
            final BrandRestorationByIdCommand cmd) {
        final var brandId = new BrandId(cmd.brandId());
        final var expectedVersion = new BrandVersion(cmd.brandVersion());

        final var brand = this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        BrandVersionGuard.ensureMatch(
                expectedVersion,
                brand.getVersion());

        final var next = brand.restored();
        final var saved = this.updatePort.update(next);

        final var event = new BrandRestored(saved.getId());
        this.eventPort.publish(event);
    }
}
