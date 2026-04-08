package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteAllVariantsUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class SoftDeleteAllVariantsProductService implements SoftDeleteAllVariantsUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void deleteByIds(
            final List<VariantId> ids) {
        final var variants = this.loadAllPort.loadAllByIds(ids);

        final var next = variants.stream()
                .map(SoftDeleteAllVariantsProductService::toSoftDeleted)
                .toList();

        final var saved = this.updateAllPort.updateAll(next);

        saved.forEach(s -> this.eventPort.publish(new VariantSoftDeleted(s.getId())));
    }

    private static Variant toSoftDeleted(
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
