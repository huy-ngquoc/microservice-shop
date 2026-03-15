package vn.edu.uit.msshop.product.brand.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.UpdateBrandInfoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
public class UpdateBrandInfoService implements UpdateBrandInfoUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final BrandViewMapper mapper;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public BrandView updateInfo(
            final UpdateBrandInfoCommand command) {
        final var brand = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new BrandNotFoundException(command.id()));

        final var nameSet = command.name().getSet();
        if (nameSet == null) {
            return this.mapper.toView(brand);
        }

        final var next = this.applyChanges(brand, nameSet, command.expectedVersion());
        if (next == null) {
            return this.mapper.toView(brand);
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new BrandUpdated(saved.getId()));

        return this.mapper.toView(saved);
    }

    private @Nullable Brand applyChanges(
            final Brand current,
            final Change.Set<BrandName> nameSet,
            final BrandVersion expectedVersion) {
        if (nameSet.value().equals(current.getName())) {
            return null;
        }

        return new Brand(
                current.getId(),
                nameSet.value(),
                current.getLogoKey(),
                expectedVersion);
    }
}
