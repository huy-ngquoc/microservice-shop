package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.application.port.in.CreateBrandUseCase;
import vn.edu.uit.msshop.product.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.domain.event.brand.BrandCreated;
import vn.edu.uit.msshop.product.domain.model.brand.Brand;
import vn.edu.uit.msshop.product.domain.model.brand.command.BrandDraft;

@Service
@RequiredArgsConstructor
public class CreateBrandService implements CreateBrandUseCase {
    private final SaveBrandPort savePort;
    private final PublishBrandEventPort eventPort;

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
