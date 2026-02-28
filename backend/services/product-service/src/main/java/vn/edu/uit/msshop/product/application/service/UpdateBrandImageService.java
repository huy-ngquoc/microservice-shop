package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandImageCommand;
import vn.edu.uit.msshop.product.application.dto.query.BrandLogoView;
import vn.edu.uit.msshop.product.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.application.port.in.UpdateBrandImageUseCase;
import vn.edu.uit.msshop.product.application.port.out.DeleteBrandLogoPort;
import vn.edu.uit.msshop.product.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.application.port.out.UploadBrandLogoPort;
import vn.edu.uit.msshop.product.domain.event.brand.BrandUpdated;

@Service
@RequiredArgsConstructor
public class UpdateBrandImageService implements UpdateBrandImageUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final UploadBrandLogoPort uploadLogoPort;
    private final DeleteBrandLogoPort deleteLogoPort;
    private final PublishBrandEventPort eventPort;

    @Override
    public BrandLogoView updateImage(
            UpdateBrandImageCommand command) {
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

        final var oldLogo = oldBrand.getLogo();
        if ((oldLogo != null) && (!oldLogo.key().equals(uploadedLogo.key()))) {
            this.deleteLogoPort.deleteByKey(oldLogo.key());
        }

        final var saved = this.savePort.save(newBrand);
        this.eventPort.publish(new BrandUpdated(saved.getId()));

        return logoView;
    }

}
