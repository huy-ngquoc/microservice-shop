package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.application.port.in.DeleteBrandLogoUseCase;
import vn.edu.uit.msshop.product.application.port.out.DeleteBrandLogoPort;
import vn.edu.uit.msshop.product.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.domain.event.brand.BrandUpdated;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class DeleteBrandLogoService implements DeleteBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final DeleteBrandLogoPort deleteLogoPort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void deleteById(
            final BrandId id) {
        final var oldBrand = this.loadPort.loadById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        final var oldLogo = oldBrand.getLogo();
        if (oldLogo == null) {
            return;
        }

        this.deleteLogoPort.deleteByKey(oldLogo.key());
        final var newBrand = oldBrand.withoutLogo();
        final var saved = this.savePort.save(newBrand);
        this.eventPort.publish(new BrandUpdated(saved.getId()));
    }
}
