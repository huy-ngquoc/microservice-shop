package vn.edu.uit.msshop.product.variant.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateAllVariantsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateAllVariantsCommand.VariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateAllVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.CheckVariantProductExistsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Service
@RequiredArgsConstructor
public class CreateAllVariantsForNewProductService implements CreateAllVariantsForNewProductUseCase {
    private final CreateAllVariantsPort createAllPort;
    private final PublishVariantEventPort eventPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional
    public List<VariantView> createAllForNewProduct(
            final CreateAllVariantsCommand command) {
        final var newVariantsList = command.variants()
                .stream()
                .map((
                        final VariantCommand variant) -> new NewVariant(
                                VariantId.newId(),
                                command.productId(),
                                variant.price(),
                                variant.traits()))
                .toList();

        final var saved = this.createAllPort.createAll(newVariantsList);
        for (final var savedVariant : saved) {
            this.eventPort.publish(new VariantCreated(savedVariant.getId()));
        }

        return saved.stream().map(this.mapper::toView).toList();
    }
}
