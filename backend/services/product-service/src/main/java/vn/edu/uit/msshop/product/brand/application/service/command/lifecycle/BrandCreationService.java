package vn.edu.uit.msshop.product.brand.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.command.BrandLifecycleCommands;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class BrandCreationService
        implements BrandLifecycleUseCases.Create {

    private final CreateBrandPort createPort;

    private final BrandViewMapper mapper;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.BRAND_LIST,
            allEntries = true)
    public BrandView create(
            final BrandLifecycleCommands.Create cmd) {
        final var brand = new NewBrand(
                BrandId.newId(),
                cmd.name());

        final var saved = this.createPort.create(brand);

        final var event = new BrandCreated(saved.getId());
        this.eventPort.publish(event);

        return this.mapper.toView(saved);
    }
}
