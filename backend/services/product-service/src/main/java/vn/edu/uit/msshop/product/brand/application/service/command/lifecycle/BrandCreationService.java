package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle.BrandCreationCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandCreationUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.BrandEventPublicationPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.command.BrandCreationPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreatedEvent;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;

@Service
@RequiredArgsConstructor
class BrandCreationService
        implements BrandCreationUseCase {

    private final BrandCreationPort createPort;

    private final BrandEventPublicationPort eventPublicationPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.BRAND_LIST,
            allEntries = true)
    public BrandView create(
            final BrandCreationCommand cmd) {
        final var brandName = new BrandName(cmd.brandName());
        final var newBrand = new NewBrand(
                BrandId.newId(),
                brandName);

        final var saved = this.createPort.create(newBrand);

        final var event = new BrandCreatedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(saved);
    }
}
