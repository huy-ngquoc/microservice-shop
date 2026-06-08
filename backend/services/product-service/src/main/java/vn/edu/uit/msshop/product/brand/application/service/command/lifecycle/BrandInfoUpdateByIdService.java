package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandInfoUpdateByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.BrandEventPublicationPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandUpdatePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandActiveLookupByIdPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandInfoUpdatedEvent;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
public class BrandInfoUpdateByIdService
        implements BrandInfoUpdateByIdUseCase {

    private final BrandActiveLookupByIdPort loadPort;
    private final BrandUpdatePort updatePort;

    private final BrandEventPublicationPort eventPublicationPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND,
                            key = "#cmd.brandId()",
                            condition = "#cmd.brandNameChange().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND_LIST,
                            allEntries = true,
                            condition = "#cmd.brandNameChange().getSet() != null")
            })
    public BrandView updateInfoById(
            final BrandInfoUpdateByIdCommand cmd) {
        final var brandId = new BrandId(cmd.brandId());
        final var nameChange = cmd.brandNameChange().map(BrandName::new);

        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var nameSet = nameChange.getSet();
        if (nameSet == null) {
            return this.mapper.toView(brand);
        }

        final var expectedVersion = new BrandVersion(cmd.brandVersion());
        BrandVersionGuard.ensureMatch(
                expectedVersion,
                brand.getVersion());

        final var next = BrandInfoUpdateByIdService.applyChanges(brand, nameSet);
        if (next == null) {
            return this.mapper.toView(brand);
        }

        final var saved = this.updatePort.update(next);

        final var event = new BrandInfoUpdatedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(saved);
    }

    private static @Nullable Brand applyChanges(
            final Brand current,
            final Change.Set<BrandName> nameSet) {
        final var applyNameResult = Change.Set.applyChange(
                nameSet,
                current.getName());
        if (!applyNameResult.changed()) {
            return null;
        }

        final var newName = applyNameResult.newValue();
        return current.rename(newName);
    }
}
