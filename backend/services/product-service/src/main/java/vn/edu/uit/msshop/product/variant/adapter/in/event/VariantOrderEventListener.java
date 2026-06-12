package vn.edu.uit.msshop.product.variant.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantSoldCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantSoldCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantSoldCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Component
@KafkaListener(
        topics = "order-variant")
@RequiredArgsConstructor
public class VariantOrderEventListener {
    private final VariantSoldCountBulkSetUseCase soldCountBulkSetUseCase;

    @KafkaHandler
    public void onSetSoldCounts(
            final SetVariantSoldCountsEvent event) {
        final var orderSoldCounts = event.details().stream()
                .map(VariantOrderEventListener::toOrderSoldCount)
                .toList();

        final var command = new SetAllVariantSoldCountsCommand(orderSoldCounts);
        this.soldCountBulkSetUseCase.execute(command);
    }

    private static VariantOrderSoldCount toOrderSoldCount(
            final SetVariantSoldCountsEvent.Detail detail) {
        return new VariantOrderSoldCount(
                new VariantId(detail.variantId()),
                new VariantSoldCountValue(detail.newTotal()));
    }
}
