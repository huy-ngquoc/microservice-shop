package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;
import vn.edu.uit.msshop.product.variant.application.port.in.command.RestoreVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadAllSoftDeletedVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestored;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class RestoreVariantsForProductService
        implements RestoreVariantsForProductUseCase {
    private final LoadAllSoftDeletedVariantsPort loadAllSoftDeletedPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;
    private final VariantUpdateRepository variantUpdateRepo;
    private final PublishProductEventPort publishProductEventPort;

    @Override
    @Transactional
    public void restoreByIds(
            final Collection<VariantId> ids) {
        final var variants = this.loadAllSoftDeletedPort
                .loadAllSoftDeletedByIds(ids);

        final var next = variants.stream()
                .map(RestoreVariantsForProductService::toRestored)
                .toList();

        final var saved = this.updateAllPort.updateAll(next);

        
        
        saved.forEach(s -> {
                this.eventPort.publish(new VariantRestored(s.getId()));
                

        });
        
    }
    private List<String> getTraits(Variant v) {
        return v.getTraits().values().stream().map(item->item.value()).toList();
    }

    private static Variant toRestored(
            final Variant variant) {
        return new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                null);
    }
}
