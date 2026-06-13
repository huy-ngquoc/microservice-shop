package vn.edu.uit.msshop.product.variant.application.service.command.image;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantImageDeleter {

    private final VariantImageStoragePort imageStoragePort;

    public void deleteQuietly(
            @Nullable
            VariantImageKey key) {
        if (key == null) {
            return;
        }

        try {
            imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Failed to delete variant image '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
