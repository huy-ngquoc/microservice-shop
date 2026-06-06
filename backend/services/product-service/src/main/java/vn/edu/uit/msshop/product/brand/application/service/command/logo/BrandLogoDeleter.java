package vn.edu.uit.msshop.product.brand.application.service.command.logo;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandLogoDeleter {

    private final BrandLogoStoragePort logoStoragePort;

    public void deleteQuietly(
            @Nullable
            BrandLogoKey key) {
        if (key == null) {
            return;
        }

        try {
            logoStoragePort.deleteLogo(key);
        } catch (final RuntimeException e) {
            log.warn("Failed to delete logo '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }

}
