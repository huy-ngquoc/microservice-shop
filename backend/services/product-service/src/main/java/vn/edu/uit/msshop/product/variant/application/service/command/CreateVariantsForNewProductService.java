package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
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

    @Override
    @Transactional
    public List<VariantView> create(
            final CreateVariantsForNewProductCommand command) {
        final var newVariantsList = command.newVariantsForNewProduct().values()
                .stream()
                .map(v -> CreateVariantsForNewProductService.toNewVariant(command.productId(), v))
                .toList();

        final var saved = this.createAllPort.createAll(newVariantsList);
        for (final var savedVariant : saved) {
            this.eventPort.publish(new VariantCreated(savedVariant.getId()));
        }

        return saved.stream().map(this.mapper::toView).toList();
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
