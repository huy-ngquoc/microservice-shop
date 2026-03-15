package vn.edu.uit.msshop.product.brand.application.service;

import java.util.ConcurrentModificationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.DeleteBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.SaveBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteBrandLogoService implements DeleteBrandLogoUseCase {
    private final LoadBrandPort loadPort;
    private final SaveBrandPort savePort;
    private final BrandLogoStoragePort logoStoragePort;
    private final PublishBrandEventPort eventPort;

    @Override
    @Transactional
    public void deleteLogo(
            final DeleteBrandLogoCommand command) {
        final var brandId = command.id();
        final var brand = this.loadPort.loadById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        final var oldKey = brand.getLogoKey();
        if (oldKey == null) {
            return;
        }

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = brand.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new ConcurrentModificationException(
                    String.format(
                            "Brand version mismatched while trying to deleting logo "
                                    + "(Expected: %s, Current: %s).",
                            expectedVersion.value(),
                            BrandVersion.unwrap(currentVersion)));
        }

        final var next = new Brand(
                brand.getId(),
                brand.getName(),
                null,
                command.expectedVersion());
        final var saved = this.savePort.save(next);

        final var event = new BrandLogoUpdated(
                saved.getId(),
                null,
                oldKey);
        this.eventPort.publish(event);

        this.deleteOldLogo(oldKey);
    }

    private void deleteOldLogo(
            final BrandLogoKey oldKey) {
        try {
            this.logoStoragePort.deleteLogo(oldKey);
        } catch (Exception e) {
            log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
        }
    }
}
