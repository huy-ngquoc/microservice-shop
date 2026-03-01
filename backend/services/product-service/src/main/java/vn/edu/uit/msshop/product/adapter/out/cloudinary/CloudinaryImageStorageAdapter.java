package vn.edu.uit.msshop.product.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.adapter.exception.ImageUploadFailedException;
import vn.edu.uit.msshop.product.application.port.out.DeleteBrandLogoPort;
import vn.edu.uit.msshop.product.application.port.out.DeleteCategoryImagePort;
import vn.edu.uit.msshop.product.application.port.out.UploadBrandLogoPort;
import vn.edu.uit.msshop.product.application.port.out.UploadCategoryImagePort;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogo;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoSize;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandLogoUrl;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImage;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageSize;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryImageUrl;

@Component
@Slf4j
public class CloudinaryImageStorageAdapter
        implements UploadCategoryImagePort, DeleteCategoryImagePort,
        UploadBrandLogoPort, DeleteBrandLogoPort {

    private static final String CATEGORY_FOLDER = "categories";
    private static final String BRAND_FOLDER = "brands";

    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudinaryOptionKeys.RESOURCE_TYPE, CloudinaryResourceType.IMAGE.value());

    private final Uploader uploader;

    public CloudinaryImageStorageAdapter(
            final Cloudinary cloudinary) {
        this.uploader = cloudinary.uploader();
    }

    @Override
    public CategoryImage upload(
            final CategoryId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {

        final var uploaded = this.uploadImage(bytes, CATEGORY_FOLDER, id.value());

        return new CategoryImage(
                new CategoryImageUrl(uploaded.url()),
                new CategoryImageKey(uploaded.publicId()),
                new CategoryImageSize(uploaded.width(), uploaded.height()));
    }

    @Override
    public BrandLogo upload(
            final BrandId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {

        final var uploaded = this.uploadImage(bytes, BRAND_FOLDER, id.value());

        return new BrandLogo(
                new BrandLogoUrl(uploaded.url()),
                new BrandLogoKey(uploaded.publicId()),
                new BrandLogoSize(uploaded.width(), uploaded.height()));
    }

    @Override
    public void deleteByKey(
            final CategoryImageKey key) {
        this.deleteByKey(key.value());
    }

    @Override
    public void deleteByKey(
            final BrandLogoKey key) {
        this.deleteByKey(key.value());
    }

    private UploadedImage uploadImage(
            final byte[] bytes,
            final String folder,
            final UUID id) {
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

    private void deleteByKey(
            final String key) {
        try {
            this.uploader.destroy(key, Collections.emptyMap());
        } catch (final Exception exception) {
            log.warn("Cannot delete image with key: {}", key, exception);
        }
    }

    private static String getString(
            final Map<?, ?> response,
            final String key) {
        return (String) response.get(key);
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

    private record UploadedImage(
            String url,
            String publicId,
            int width,
            int height) {
    }
}
