package vn.edu.uit.msshop.product.brand.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandInfoCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.UpdateBrandInfoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.mutation.BrandUpdateInfo;

@Service
@RequiredArgsConstructor
public class UpdateBrandInfoService implements UpdateBrandInfoUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @SuppressWarnings("ReferenceEquality")
    public void updateInfo(
            final UpdateBrandInfoCommand command) {
        final var brand = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new BrandNotFoundException(command.id()));

        final var update = BrandUpdateInfo.builder()
                .name(command.name().apply(brand.getName()))
                .build();
        final var next = brand.applyUpdateInfo(update);

        if (next == brand) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new BrandUpdated(saved.getId()));
    }
}
