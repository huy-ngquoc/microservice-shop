package vn.edu.uit.msshop.product.variant.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.exception.VariantProductNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.UpdateVariantInfoUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.CheckVariantProductExistsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.mutation.VariantChangeInfo;

@Service
@RequiredArgsConstructor
public class UpdateVariantInfoService implements UpdateVariantInfoUseCase {
    private final LoadVariantPort loadPort;
    private final SaveVariantPort savePort;
    private final CheckVariantProductExistsPort checkProductExistsPort;
    private final PublishVariantEventPort eventPort;

    public void updateInfo(
            final UpdateVariantInfoCommand command) {
        final var variant = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new VariantNotFoundException(command.id()));

        final var productId = command.productId().fold(
                () -> variant.getProductId(),
                this::requireProductExists);

        final var changeInfo = new VariantChangeInfo(
                productId,
                command.price().apply(variant.getPrice()),
                command.traits().apply(variant.getTraits()));

        final var next = variant.applyChangeInfo(changeInfo);
        if (next == variant) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new VariantUpdated(saved.getId()));
    }

    private VariantProductId requireProductExists(
            final VariantProductId newProductId) {
        if (this.checkProductExistsPort.existsById(newProductId)) {
            throw new VariantProductNotFoundException(newProductId);
        }

        return newProductId;
    }
}
