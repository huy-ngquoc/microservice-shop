package vn.edu.uit.msshop.product.category.adapter.out.image;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.port.out.VerifyCategoryImageKeyPort;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryImageVerifyAdapter implements VerifyCategoryImageKeyPort {
    private static final String TEMP_FOLDER = "temp";

    private final Cloudinary cloudinary;

    @Override
    public boolean existsInTemp(
            CategoryImageKey key) {
        try {
            final var result = this.cloudinary.api()
                    .resource(TEMP_FOLDER + "/" + key.value(), Map.of());

            return (result != null) && result.containsKey("public_id");
        } catch (final Exception exception) {
            log.warn("Image key '{}' not found in temp storage", key.value(), exception);
            return false;
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
