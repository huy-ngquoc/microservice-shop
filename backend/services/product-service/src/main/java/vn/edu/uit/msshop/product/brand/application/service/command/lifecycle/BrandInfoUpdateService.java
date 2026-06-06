package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.service.command.support.BrandVersionGuard;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
public class BrandInfoUpdateService
        implements BrandLifecycleUseCases.UpdateInfo {

    private final LoadBrandPort loadPort;
    private final UpdateBrandPort updatePort;

    private final BrandViewMapper mapper;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND,
                            key = "#cmd.id().value()",
                            condition = "#cmd.name().getSet() != null"),
                    @CacheEvict(
                            cacheNames = CacheNames.BRAND_LIST,
                            allEntries = true,
                            condition = "#cmd.name().getSet() != null")
            })
    public BrandView updateInfo(
            final BrandLifecycleCommands.UpdateInfo cmd) {
        final var brand = this.loadPort.loadById(cmd.id())
                .orElseThrow(() -> new BrandNotFoundException(cmd.id()));

        final var nameSet = cmd.name().getSet();
        if (nameSet == null) {
            return this.mapper.toView(brand);
        }

        BrandVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                brand.getVersion());

        final var next = BrandInfoUpdateService.applyChanges(brand, nameSet);
        if (next == null) {
            return this.mapper.toView(brand);
        }

        final var saved = this.updatePort.update(next);

        final var event = new BrandUpdated(saved.getId());
        this.eventPort.publish(event);

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
