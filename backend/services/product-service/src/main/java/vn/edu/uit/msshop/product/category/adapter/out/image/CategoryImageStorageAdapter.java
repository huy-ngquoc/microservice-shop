package vn.edu.uit.msshop.product.category.adapter.out.image;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.port.out.image.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryImageStorageAdapter implements CategoryImageStoragePort {
    private static final String TEMP_FOLDER = "temp";
    private static final String CATEGORY_FOLDER = "categories";

    private final Cloudinary cloudinary;

    @Override
    public boolean existsAsTemp(
            final CategoryImageKey key) {
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
            final CategoryImageKey key) {
        final var fromPublicId = TEMP_FOLDER + "/" + key.value();
        final var toPublicId = CATEGORY_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void unpublishImage(
            final CategoryImageKey key) {
        final var fromPublicId = CATEGORY_FOLDER + "/" + key.value();
        final var toPublicId = TEMP_FOLDER + "/" + key.value();

        this.renamePublicId(fromPublicId, toPublicId);
    }

    @Override
    public void deleteImage(
            final CategoryImageKey key) {
        try {
            this.cloudinary.uploader().destroy(CATEGORY_FOLDER + "/" + key.value(), Map.of());
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

// @Component
// @RequiredArgsConstructor
// public class CategoryImageVerifyAdapter implements VerifyCategoryImageKeyPort
// {
// private final ImageServiceFeignClient imageServiceClient;
// @Override
// public boolean existsInTemp(CategoryImageKey key) {
// return this.imageServiceClient.existsInTemp(key.value());
// }
// }
// // Feign Client gọi sang image-service
// @FeignClient(name = "image-service")
// public interface ImageServiceFeignClient {
// @GetMapping("/images/temp/{key}/exists")
// boolean existsInTemp(@PathVariable String key);
// }
