package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.repository.VariantDeletedRepository;
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
public class SoftDeleteAllVariantsService implements SoftDeleteAllVariantsUseCase {
    private final LoadAllVariantsPort loadAllPort;
    private final UpdateAllVariantsPort updateAllPort;
    private final PublishVariantEventPort eventPort;
    private final VariantDeletedRepository variantDeletedRepo;
    private final PublishProductEventPort publishProductEventPort;
    


    @Override
    @Transactional
    public void deleteByIds(
            final Set<VariantId> ids) {
        final var variantById = this.loadAllPort.loadAllByIds(ids);

        final var next = variantById.values().stream()
                .map(SoftDeleteAllVariantsService::toSoftDeleted)
                .toList();

        final var saved = this.updateAllPort.updateAll(next);
        List<VariantDeletedDocument> eventDocuments = new ArrayList<>();

        saved.forEach(s ->{ 
            this.eventPort.publish(new VariantSoftDeleted(s.getId()));
            VariantDeletedDocument eventDocument = new VariantDeletedDocument(UUID.randomUUID(), s.getId().value(), "PENDING", 0, Instant.now(), null, null);
            eventDocuments.add(eventDocument);
        });
        final var savedEvents = variantDeletedRepo.saveAll(eventDocuments);
        savedEvents.forEach(e->publishProductEventPort.publishVariantDeleted(e));
    }

    private static Variant toSoftDeleted(
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
                VariantDeletionTime.now());
    }
}
