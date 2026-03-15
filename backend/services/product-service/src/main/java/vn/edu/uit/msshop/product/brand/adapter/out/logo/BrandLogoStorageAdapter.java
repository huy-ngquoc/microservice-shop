package vn.edu.uit.msshop.product.brand.adapter.out.logo;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.port.out.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandLogoStorageAdapter
        implements BrandLogoStoragePort {
    private static final String TEMP_FOLDER = "temp";
    private static final String BRAND_FOLDER = "brands";

    private final Cloudinary cloudinary;

    @Override
    public boolean existsAsTemp(
            final BrandLogoKey key) {
        try {
            final var result = this.cloudinary.api()
                    .resource(TEMP_FOLDER + "/" + key.value(), Map.of());

            return (result != null) && result.containsKey("public_id");
        } catch (final Exception exception) {
            log.warn("Image key '{}' not found in temp storage", key.value(), exception);
            return false;
        }
    }

    @Override
    public void publishLogo(
            final BrandLogoKey key) {
        final var fromPublicId = TEMP_FOLDER + "/" + key.value();
        final var toPublicId = BRAND_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void unpublishLogo(
            final BrandLogoKey key) {
        final var fromPublicId = BRAND_FOLDER + "/" + key.value();
        final var toPublicId = TEMP_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void deleteLogo(
            final BrandLogoKey key) {
        try {
            this.cloudinary.uploader().destroy(BRAND_FOLDER + "/" + key.value(), Map.of());
        } catch (final Exception e) {
            throw new RuntimeException("Failed to delete image: " + key.value(), e);
        }
    }

    private void renamePublicId(
            final String fromPublicId,
            final String toPublicId) {
        try {
            this.cloudinary.uploader().rename(fromPublicId, toPublicId, Map.of());
        } catch (final Exception e) {
            throw new RuntimeException("Failed to rename image: " + fromPublicId + " → " + toPublicId, e);
        }
    }
}
