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
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandLogoDeletionByIdService
        implements BrandLogoDeletionByIdUseCase {

    private final LoadBrandPort loadPort;
    private final UpdateBrandPort updatePort;

    private final BrandLogoDeleter deleter;

    private final PublishBrandEventPort eventPort;

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

        final var event = new BrandLogoUpdated(
                saved.getId(),
                saved.getLogoKey(),
                oldKey);
        this.eventPort.publish(event);

        this.deleter.deleteQuietly(oldKey);
    }
}
