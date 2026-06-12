package vn.edu.uit.msshop.product.variant.adapter.in.event;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantStockCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantStockCountsCommand;
import vn.edu.uit.msshop.product.variant.application.port.in.command.count.VariantStockCountBulkSetUseCase;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Component
@KafkaListener(
        topics = "inventory-variant")
@RequiredArgsConstructor
public class VariantInventoryEventListener {
    private final VariantStockCountBulkSetUseCase stockCountBulkSetUseCase;

    @KafkaHandler
    public void onSetStockCounts(
            final SetVariantStockCountsEvent event) {
        final var stockCounts = event.details().stream()
                .map(VariantInventoryEventListener::toInventoryStockCount)
                .toList();
        final var command = new SetAllVariantStockCountsCommand(stockCounts);

        this.stockCountBulkSetUseCase.execute(command);
    }

    private static VariantInventoryStockCount toInventoryStockCount(
            final SetVariantStockCountsEvent.Detail detail) {
        return new VariantInventoryStockCount(
                new VariantId(detail.variantId()),
                new VariantStockCountValue(detail.value()));
    }
}
