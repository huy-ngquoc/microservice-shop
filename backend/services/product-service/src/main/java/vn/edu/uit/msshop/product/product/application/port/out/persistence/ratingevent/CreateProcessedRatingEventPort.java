package vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent;

import java.util.UUID;

public interface CreateProcessedRatingEventPort {
    void create(
            final UUID eventId);
}
