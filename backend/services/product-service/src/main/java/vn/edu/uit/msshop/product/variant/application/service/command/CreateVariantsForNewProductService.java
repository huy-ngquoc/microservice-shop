package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
public class CreateVariantsForNewProductService implements CreateVariantsForNewProductUseCase {
    private final CreateAllVariantsPort createAllVariantsPort;
    private final InitializeAllVariantSoldCountsPort initializeAllSoldCountsPort;
    private final PublishVariantEventPort eventPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional
    public List<VariantView> create(
            final CreateVariantsForNewProductCommand command) {
        final var saved = this.createVariants(command);
        final var soldCountByVariantId = this.initializeSoldCounts(saved);
        this.publishCreatedEvents(saved);
        return this.toViews(saved, soldCountByVariantId);
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

    private List<Variant> createVariants(
            final CreateVariantsForNewProductCommand command) {
        final var newVariants = command.newVariantsForNewProduct().values().stream()
                .map(v -> CreateVariantsForNewProductService.toNewVariant(command.productId(), v))
                .toList();
        return this.createAllVariantsPort.createAll(newVariants);
    }

    private Map<VariantId, VariantSoldCount> initializeSoldCounts(
            final List<Variant> saved) {
        final var newSoldCounts = saved.stream()
                .map(v -> new NewVariantSoldCount(v.getId(), v.getProductId()))
                .toList();
        return this.initializeAllSoldCountsPort.initializeAll(newSoldCounts);
    }

    private List<VariantView> toViews(
            final List<Variant> saved,
            final Map<VariantId, VariantSoldCount> soldCountByVariantId) {
        return saved.stream()
                .map(v -> this.toView(v, soldCountByVariantId))
                .toList();
    }

    private VariantView toView(
            final Variant variant,
            final Map<VariantId, VariantSoldCount> soldCountByVariantId) {
        final var soldCount = soldCountByVariantId.get(variant.getId());
        Objects.requireNonNull(
                soldCount,
                () -> "Sold count must be initialized for variant " + variant.getId());

        return this.mapper.toView(variant, soldCount);
    }

    private void publishCreatedEvents(
            final List<Variant> saved) {
        for (final var variant : saved) {
            this.eventPort.publish(new VariantCreated(variant.getId()));
        }
    }
}
