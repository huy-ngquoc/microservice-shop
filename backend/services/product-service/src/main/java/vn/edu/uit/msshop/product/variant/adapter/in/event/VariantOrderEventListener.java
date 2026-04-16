package vn.edu.uit.msshop.product.variant.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.IncreaseVariantSoldCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.command.IncreaseVariantSoldCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.IncreaseVariantSoldCountsUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@KafkaListener(
        topics = "order-variant",
        groupId = "product-service")
@RequiredArgsConstructor
public class VariantOrderEventListener {
    private final IncreaseVariantSoldCountsUseCase increaseVariantSoldCountsUseCase;

    @KafkaHandler
    public void onIncreaseSoldCounts(
            final IncreaseVariantSoldCountsEvent event) {
        final var items = event.details().stream()
                .map(VariantOrderEventListener::toCommandItem)
                .toList();

        this.increaseVariantSoldCountsUseCase.execute(
                new IncreaseVariantSoldCountsCommand(event.eventId(), items));
    }

    private static IncreaseVariantSoldCountsCommand.Item toCommandItem(
            final IncreaseVariantSoldCountsEvent.Detail detail) {
        return new IncreaseVariantSoldCountsCommand.Item(
                new VariantId(detail.variantId()),
                detail.amount());
    }
}
