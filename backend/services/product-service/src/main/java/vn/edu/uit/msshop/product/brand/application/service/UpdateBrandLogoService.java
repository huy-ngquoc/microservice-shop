package vn.edu.uit.msshop.product.brand.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.UpdateBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.DeleteBrandLogoPort;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.UploadBrandLogoPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;

@Service
@RequiredArgsConstructor
public class UpdateBrandLogoService implements UpdateBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final UploadBrandLogoPort uploadLogoPort;
    private final DeleteBrandLogoPort deleteLogoPort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    @SuppressWarnings("ReferenceEquality")
    public BrandLogoView updateImage(
            UpdateBrandLogoCommand command) {
        final var oldBrand = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new BrandNotFoundException(command.id()));

        final var uploadedLogo = this.uploadLogoPort.upload(
                command.id(),
                command.bytes(),
                command.originalFilename(),
                command.contentType());

        final var newBrand = oldBrand.withLogo(uploadedLogo);

        final var logoView = new BrandLogoView(
                uploadedLogo.url().value(),
                uploadedLogo.size().width(),
                uploadedLogo.size().height());

        if (newBrand == oldBrand) {
            return logoView;
        }

        final var saved = this.savePort.save(newBrand);

        final var oldLogo = oldBrand.getLogo();
        if ((oldLogo != null) && !oldLogo.key().equals(uploadedLogo.key())) {
            this.deleteLogoPort.deleteByKey(oldLogo.key());
        }

        this.eventPort.publish(new BrandUpdated(saved.getId()));

        return logoView;
    }

}
