package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.application.dto.integration.RatingCreatedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingDeletedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingUpdatedIntegrationEvent;

public interface PublishRatingIntegrationEventPort {
    void publishCreated(
            final RatingCreatedIntegrationEvent event);

    void publishUpdated(
            final RatingUpdatedIntegrationEvent event);

    void publishDeleted(
            final RatingDeletedIntegrationEvent event);
}
