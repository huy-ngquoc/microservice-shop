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
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;

@Service
@RequiredArgsConstructor
public class BrandCreationService
        implements BrandCreationUseCase {

    private final CreateBrandPort createPort;

    private final BrandViewMapper mapper;
    private final PublishBrandEventPort eventPort;

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

        final var event = new BrandCreated(saved.getId());
        this.eventPort.publish(event);

        return this.mapper.toView(saved);
    }
}
