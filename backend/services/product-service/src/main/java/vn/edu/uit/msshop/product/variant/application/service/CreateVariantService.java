package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.CheckVariantProductExistsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;

@Service
@RequiredArgsConstructor
public class CreateVariantService implements CreateVariantUseCase {
    private final CheckVariantProductExistsPort checkProductExistsPort;
    private final SaveVariantPort savePort;
    private final PublishVariantEventPort eventPort;

    @Override
    public void create(
            final CreateVariantCommand command) {
        if (!this.checkProductExistsPort.existsById(command.productId())) {
            throw new VariantProductNotFoundException(command.productId());
        }

        final var variant = new Variant(
                VariantId.newId(),
                command.productId(),
                command.image(),
                command.price(),
                command.sold(),
                command.traits());

        final var saved = this.savePort.save(variant);
        this.eventPort.publish(new VariantCreated(saved.getId()));
    }

}
