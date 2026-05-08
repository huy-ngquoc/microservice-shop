package vn.edu.uit.msshop.product.variant.application.service.command;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.image.VariantImageStoragePort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantPurged;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardDeleteVariantsForProductService implements HardDeleteVariantsForProductUseCase {
    private final LoadVariantsForProductPort loadForProductPort;
    private final DeleteVariantsForProductPort deleteForProductPort;
    private final DeleteAllVariantSoldCountsPort deleteAllSoldCountsPort;
    private final VariantImageStoragePort imageStoragePort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void purgeByProductId(
            final VariantProductId productId) {
        final var variants = this.loadForProductPort.loadAllByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        final var ids = variants.stream()
                .map(Variant::getId)
                .toList();

        this.deleteForProductPort.deleteByProductId(productId);
        this.deleteAllSoldCountsPort.deleteAllByIds(ids);

        variants.forEach(v -> this.deleteImage(v.getImageKey()));
        variants.forEach(v -> this.eventPort.publish(new VariantPurged(v.getId())));
    }

    private void deleteImage(
            @Nullable
            final VariantImageKey key) {
        if (key == null) {
            return;
        }
        try {
            this.imageStoragePort.deleteImage(key);
        } catch (final RuntimeException e) {
            log.warn("Hard delete: failed to delete image '{}', manual cleanup required",
                    key.value(),
                    e);
        }
    }
}
