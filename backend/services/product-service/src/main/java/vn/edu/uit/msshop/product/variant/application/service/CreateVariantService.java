package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.CheckVariantProductExistsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.CreateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Service
@RequiredArgsConstructor
public class CreateVariantService implements CreateVariantUseCase {
    private final CheckVariantProductExistsPort checkProductExistsPort;
    private final CreateVariantPort createPort;
    private final VariantViewMapper mapper;
    private final PublishVariantEventPort eventPort;

    @Override
    public VariantView create(
            final CreateVariantCommand command) {
        if (!this.checkProductExistsPort.existsById(command.productId())) {
            throw new VariantProductNotFoundException(command.productId());
        }

        final var variant = new NewVariant(
                VariantId.newId(),
                command.productId(),
                command.price(),
                command.traits());

        final var saved = this.createPort.create(variant);
        this.eventPort.publish(new VariantCreated(saved.getId()));

        return this.mapper.toView(saved);
    }

}
