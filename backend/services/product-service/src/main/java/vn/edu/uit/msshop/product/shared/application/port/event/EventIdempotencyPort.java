package vn.edu.uit.msshop.product.shared.application.port.event;

import java.util.UUID;

public interface EventIdempotencyPort {
    boolean tryAcquire(
            final UUID eventId);
}
