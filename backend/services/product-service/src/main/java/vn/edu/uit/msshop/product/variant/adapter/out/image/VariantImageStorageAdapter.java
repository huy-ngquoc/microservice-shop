package vn.edu.uit.msshop.product.variant.adapter.out.image;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantImageStorageAdapter
        implements VariantImageStoragePort {
    private static final String TEMP_FOLDER = "temp";
    private static final String VARIANTS_FOLDER = "variants";

    private final Cloudinary cloudinary;

    @Override
    public boolean existsAsTemp(
            final VariantImageKey key) {
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
    public void publishImage(
            final VariantImageKey key) {
        final var fromPublicId = TEMP_FOLDER + "/" + key.value();
        final var toPublicId = VARIANTS_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void unpublishImage(
            final VariantImageKey key) {
        final var fromPublicId = VARIANTS_FOLDER + "/" + key.value();
        final var toPublicId = TEMP_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void deleteImage(
            final VariantImageKey key) {
        try {
            this.cloudinary.uploader().destroy(VARIANTS_FOLDER + "/" + key.value(), Map.of());
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
