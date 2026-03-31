package vn.uit.edu.msshop.recommendation.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteVariantEvent {
    private UUID eventId;
    private UUID variantId;
}
