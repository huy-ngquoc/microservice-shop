package vn.edu.uit.msshop.product.brand.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.port.in.CreateBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.mutation.BrandDraft;

@Service
@RequiredArgsConstructor
public class CreateBrandService implements CreateBrandUseCase {
    private final SaveBrandPort savePort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void create(
            final CreateBrandCommand command) {
        final var draft = BrandDraft.builder()
                .name(command.name())
                .build();
        final var brand = Brand.create(draft);
        final var saved = this.savePort.save(brand);

        this.eventPort.publish(new BrandCreated(saved.getId()));
    }
}
