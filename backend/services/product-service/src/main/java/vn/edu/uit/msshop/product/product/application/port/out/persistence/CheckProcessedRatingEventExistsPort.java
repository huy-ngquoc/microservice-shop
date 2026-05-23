package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.UUID;

public interface CheckProcessedRatingEventExistsPort {
    boolean exists(
            final UUID eventId);
}
