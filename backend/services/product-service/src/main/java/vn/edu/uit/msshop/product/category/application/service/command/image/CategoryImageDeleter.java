package vn.edu.uit.msshop.product.category.application.service.command.image;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.port.out.image.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryImageDeleter {

    private final CategoryImageStoragePort imageStoragePort;

    public void deleteQuietly(
            @Nullable
            CategoryImageKey key) {
        if (key == null) {
            return;
        }

        try {
            imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Failed to delete category image '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
