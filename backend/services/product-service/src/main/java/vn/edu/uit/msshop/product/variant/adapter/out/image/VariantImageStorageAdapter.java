package vn.edu.uit.msshop.product.variant.adapter.out.image;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.NotFound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.shared.adapter.exception.ImageDeletionFailedException;
import vn.edu.uit.msshop.shared.adapter.exception.ImageRenameFailedException;
import vn.edu.uit.msshop.shared.adapter.exception.ImageStorageQueryFailedException;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantImageStorageAdapter implements VariantImageStoragePort {
    private static final String VARIANTS_FOLDER = "variants";

    private final Cloudinary cloudinary;

    @Override
    public boolean existsAsTemp(
            final VariantImageKey key) {
        try {
            final var result = this.cloudinary
                    .api()
                    .resource(CloudinaryFolders.TEMP + "/" + key.value(), Map.of());

            return (result != null) && result.containsKey("public_id");
        } catch (final NotFound _) {
            log.debug("Image key '{}' not found in temp storage", key.value());
            return false;
        } catch (final Exception e) {
            throw new ImageStorageQueryFailedException(e);
        }
    }

    @Override
    public void publishImage(
            final VariantImageKey key) {
        final var fromPublicId = CloudinaryFolders.TEMP + "/" + key.value();
        final var toPublicId = VARIANTS_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void unpublishImage(
            final VariantImageKey key) {
        final var fromPublicId = VARIANTS_FOLDER + "/" + key.value();
        final var toPublicId = CloudinaryFolders.TEMP + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void deleteImage(
            final VariantImageKey key) {
        try {
            this.cloudinary
                    .uploader()
                    .destroy(VARIANTS_FOLDER + "/" + key.value(), Map.of());
        } catch (final IOException e) {
            throw new ImageDeletionFailedException("Failed to delete image: " + key.value(), e);
        }
    }

    private void renamePublicId(
            final String fromPublicId,
            final String toPublicId) {
        try {
            this.cloudinary
                    .uploader()
                    .rename(fromPublicId, toPublicId, Map.of());
        } catch (final IOException e) {
            final var msg = String.format(
                    "Failed to rename image from `%s` to `%s`",
                    fromPublicId,
                    toPublicId);
            throw new ImageRenameFailedException(msg, e);
        }
    }
}
