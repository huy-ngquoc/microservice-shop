package vn.edu.uit.msshop.product.brand.application.service.command.logo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.logo.BrandLogoDeletionByIdCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.logo.BrandLogoDeletionByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.BrandEventPublicationPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandUpdatePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandActiveLookupByIdPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdatedEvent;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

@Service
@RequiredArgsConstructor
@Slf4j
class BrandLogoDeletionByIdService
        implements BrandLogoDeletionByIdUseCase {

    private final BrandActiveLookupByIdPort loadPort;
    private final BrandUpdatePort updatePort;

    private final BrandLogoDeleter deleter;

    private final BrandEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND,
                            key = "#cmd.brandId()"),
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND_LIST,
                            allEntries = true)
            })
    public void deleteById(
            final BrandLogoDeletionByIdCommand cmd) {
        final var brandId = new BrandId(cmd.brandId());
        final var expectedVersion = new BrandVersion(cmd.brandVersion());

        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var oldKey = brand.getLogoKey();
        if (oldKey == null) {
            return;
        }

        BrandVersionGuard.ensureMatch(
                expectedVersion,
                brand.getVersion());

        final var next = brand.removeLogoKey();
        final var saved = this.updatePort.update(next);

        final var event = new BrandLogoUpdatedEvent(
                saved.getId(),
                saved.getLogoKey(),
                oldKey);
        this.eventPublicationPort.publishEvent(event);

        this.deleter.deleteQuietly(oldKey);
    }
}
