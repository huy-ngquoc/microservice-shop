package vn.edu.uit.msshop.product.variant.application.port.out.event;

import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantSoftDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantUpdatedIntegrationEvent;

public interface PublishVariantIntegrationEventPort {
  void publishUpdated(VariantUpdatedIntegrationEvent event);

  void publishSoftDeleted(VariantSoftDeletedIntegrationEvent event);
}
