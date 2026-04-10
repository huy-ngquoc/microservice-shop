package vn.edu.uit.msshop.product.shared.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.adapter.exception.ImageUploadFailedException;

@Slf4j
public abstract class CloudinaryImageStorageAdapterBase {
    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudinaryOptionKeys.RESOURCE_TYPE, CloudinaryResourceType.IMAGE.value());

    private final Uploader uploader;

    protected CloudinaryImageStorageAdapterBase(
            final Cloudinary cloudinary) {
        this.uploader = cloudinary.uploader();
    }

    protected UploadedImage uploadImage(
            final byte[] bytes,
            final String folder,
            final String id) {
        try {
            final var uploadOptions = Map.<String, String>ofEntries(
                    Map.entry(CloudinaryOptionKeys.ASSET_FOLDER, folder + "/" + id),
                    UPLOAD_OPTION_RESOURCE_TYPE);

            final var response = this.uploader.upload(bytes, uploadOptions);

            return new UploadedImage(
                    getString(response, CloudinaryResultKeys.SECURE_URL),
                    getString(response, CloudinaryResultKeys.PUBLIC_ID),
                    getInt(response, CloudinaryResultKeys.WIDTH),
                    getInt(response, CloudinaryResultKeys.HEIGHT));
        } catch (final IOException exception) {
            throw new ImageUploadFailedException(exception);
        }
    }

    protected void deleteByKey(
            final String key) {
        try {
            this.uploader.destroy(key, Map.of());
        } catch (final Exception exception) {
            log.warn("Cannot delete image with key: {}", key, exception);
        }
    }

    private static String getString(
            final Map<?, ?> response,
            final String key) {
        final var value = response.get(key);
        if (!(value instanceof String str)) {
            throw new IllegalStateException("Missing or invalid string field: " + key);
        }

        return str;
    }

    private static int getInt(
            final Map<?, ?> response,
            final String key) {
        final var value = response.get(key);
        if (!(value instanceof Number number)) {
            throw new IllegalStateException("Missing or invalid numeric field: " + key);
        }

        return number.intValue();
    }

    protected record UploadedImage(
            String url,
            String publicId,
            int width,
            int height) {
    }
}
