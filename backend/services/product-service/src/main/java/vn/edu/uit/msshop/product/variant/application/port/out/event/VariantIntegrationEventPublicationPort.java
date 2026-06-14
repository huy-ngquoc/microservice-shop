package vn.edu.uit.msshop.product.variant.application.port.out.event;

import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantSoftDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantUpdatedIntegrationEvent;

public interface VariantIntegrationEventPublicationPort {
    void publishUpdated(
            final VariantUpdatedIntegrationEvent event);

    void publishSoftDeleted(
            final VariantSoftDeletedIntegrationEvent event);
}
