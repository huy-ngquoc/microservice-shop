package vn.edu.uit.msshop.product.variant.application.service.command;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.repository.VariantUpdateRepository;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
public class CreateVariantsForNewProductService implements CreateVariantsForNewProductUseCase {
    private final CreateAllVariantsPort createAllPort;
    private final PublishVariantEventPort eventPort;
    private final VariantViewMapper mapper;
    private final VariantUpdateRepository variantUpdateRepo;
    private final PublishProductEventPort publishProductEventPort;

    @Override
    @Transactional
    public List<VariantView> create(
            final CreateVariantsForNewProductCommand command) {
        final var newVariantsList = command.newVariantsForNewProduct().values()
                .stream()
                .map(v -> CreateVariantsForNewProductService.toNewVariant(command.productId(), v))
                .toList();

        final var saved = this.createAllPort.createAll(newVariantsList);
        List<VariantUpdateDocument> eventDocuments = new ArrayList<>();
        for (final var savedVariant : saved) {
            VariantUpdateDocument eventDocument = new VariantUpdateDocument(UUID.randomUUID(), savedVariant.getId().value(), savedVariant.getProductId().value(), "",
                 savedVariant.getPrice().value(), getTraits(savedVariant), savedVariant.getImageKey().value(), "PENDING", 0, Instant.now(), null, null);
                 eventDocuments.add(eventDocument);
            this.eventPort.publish(new VariantCreated(savedVariant.getId()));
        }
        final var savedEvents=variantUpdateRepo.saveAll(eventDocuments);
        for(final var savedEvent:savedEvents) {
            publishProductEventPort.publishVariantUpdated(savedEvent);
        }


        return saved.stream().map(this.mapper::toView).toList();
    }
    private List<String> getTraits(Variant v) {
        return v.getTraits().values().stream().map(item->item.value()).toList();
    }

    private static NewVariant toNewVariant(
            final VariantProductId productId,
            final NewVariantForNewProduct newInputs) {
        return new NewVariant(
                VariantId.newId(),
                productId,
                newInputs.price(),
                newInputs.traits(),
                newInputs.targets());
    }
}
