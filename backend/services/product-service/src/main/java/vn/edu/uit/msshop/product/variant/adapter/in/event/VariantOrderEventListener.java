package vn.edu.uit.msshop.product.variant.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantSoldCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SetVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@KafkaListener(
        topics = "order-variant",
        groupId = "product-service")
@RequiredArgsConstructor
public class VariantOrderEventListener {
    private final SetVariantSoldCountsUseCase setVariantSoldCountsUseCase;

    @KafkaHandler
    public void onSetSoldCounts(
            final SetVariantSoldCountsEvent event) {
        final var orderSoldCounts = event.details().stream()
                .map(VariantOrderEventListener::toOrderSoldCount)
                .toList();

        this.setVariantSoldCountsUseCase.execute(orderSoldCounts);
    }

    private static VariantOrderSoldCount toOrderSoldCount(
            final SetVariantSoldCountsEvent.Detail detail) {
        return new VariantOrderSoldCount(
                new VariantId(detail.variantId()),
                detail.newTotal());
    }
}
