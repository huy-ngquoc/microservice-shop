package vn.uit.edu.msshop.order.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class OrderCreated {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();

    private final Instant occurrenceTime = Instant.now();

    private final OrderId orderId;
}