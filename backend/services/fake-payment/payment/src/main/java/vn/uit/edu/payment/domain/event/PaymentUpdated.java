package vn.uit.edu.payment.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class PaymentUpdated {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();

    private final Instant occurrenceTime = Instant.now();

    private final PaymentId paymentId;
}