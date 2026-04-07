package vn.edu.uit.msshop.product.variant.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.in.command.HardDeleteVariantsForProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.DeleteVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantsForProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantPurged;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Service
@RequiredArgsConstructor
public class HardDeleteVariantsForProductService
        implements HardDeleteVariantsForProductUseCase {
    private final LoadVariantsForProductPort loadForProductPort;
    private final DeleteVariantsForProductPort deleteForProductPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void purgeByProductId(
            final VariantProductId productId) {
        final var variants = this.loadForProductPort.loadByProductId(productId);
        if (variants.isEmpty()) {
            return;
        }

        this.deleteForProductPort.deleteByProductId(productId);
        variants.stream().forEach(v -> this.eventPort.publish(new VariantPurged(v.getId())));
    }

}
