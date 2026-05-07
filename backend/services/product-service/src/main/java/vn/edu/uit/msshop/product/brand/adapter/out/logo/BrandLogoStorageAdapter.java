package vn.edu.uit.msshop.product.brand.adapter.out.logo;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.NotFound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.adapter.exception.ImageDeletionFailedException;
import vn.edu.uit.msshop.shared.adapter.exception.ImageRenameFailedException;
import vn.edu.uit.msshop.shared.adapter.exception.ImageStorageQueryFailedException;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryFolders;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandLogoStorageAdapter implements BrandLogoStoragePort {
  public static final String BRAND_FOLDER = "brands";

  private final Cloudinary cloudinary;

  @Override
  public boolean existsAsTemp(final BrandLogoKey key) {
    try {
      final var result =
          this.cloudinary.api().resource(CloudinaryFolders.TEMP + "/" + key.value(), Map.of());

      return (result != null) && result.containsKey("public_id");
    } catch (final NotFound e) {
      log.debug("Image key '{}' not found in temp storage", key.value());
      return false;
    } catch (final Exception e) {
      throw new ImageStorageQueryFailedException(e);
    }
  }

  @Override
  public void publishLogo(final BrandLogoKey key) {
    final var fromPublicId = CloudinaryFolders.TEMP + "/" + key.value();
    final var toPublicId = BRAND_FOLDER + "/" + key.value();

    this.renamePublicId(fromPublicId, toPublicId);
  }

  @Override
  public void unpublishLogo(final BrandLogoKey key) {
    final var fromPublicId = BRAND_FOLDER + "/" + key.value();
    final var toPublicId = CloudinaryFolders.TEMP + "/" + key.value();

    this.renamePublicId(fromPublicId, toPublicId);
  }

  @Override
  public void deleteLogo(final BrandLogoKey key) {
    try {
      this.cloudinary.uploader().destroy(BRAND_FOLDER + "/" + key.value(), Map.of());
    } catch (final IOException e) {
      throw new ImageDeletionFailedException("Failed to delete image: " + key.value(), e);
    }
  }

  private void renamePublicId(final String fromPublicId, final String toPublicId) {
    try {
      this.cloudinary.uploader().rename(fromPublicId, toPublicId, Map.of());
    } catch (final IOException e) {
      throw new ImageRenameFailedException(
          "Failed to rename image: " + fromPublicId + " → " + toPublicId, e);
    }
  }
}
