package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandRestorationByIdCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandRestorationByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.BrandEventPublicationPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandUpdatePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandRestoredEvent;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

@Service
@RequiredArgsConstructor
class BrandRestorationByIdService
        implements BrandRestorationByIdUseCase {

    private final BrandSoftDeletedLookupByIdPort loadSoftDeletedPort;
    private final BrandUpdatePort updatePort;

    private final BrandEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.BRAND_LIST,
            allEntries = true)
    public void restoreById(
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

        final var event = new BrandRestoredEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);
    }
}
