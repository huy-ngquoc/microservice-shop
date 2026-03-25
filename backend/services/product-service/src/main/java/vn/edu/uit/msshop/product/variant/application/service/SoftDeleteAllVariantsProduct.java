package vn.edu.uit.msshop.product.variant.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.SoftDeleteAllVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Service
@RequiredArgsConstructor
public class SoftDeleteAllVariantsProduct implements SoftDeleteAllVariantsUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void deleteByIds(
            final List<VariantId> ids) {
        final var variants = this.loadAllPort.loadByIds(ids);

        final var next = variants.stream()
                .map(this::toSoftDeleted)
                .toList();

        final var saved = this.updateAllPort.updateAll(next);

        saved.forEach(s -> this.eventPort.publish(new VariantSoftDeleted(s.getId())));
    }

    private Variant toSoftDeleted(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getPrice(),
                variant.getSoldCount(),
                variant.getTraits(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());
    }
}
