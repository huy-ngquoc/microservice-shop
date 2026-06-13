package vn.edu.uit.msshop.product.variant.adapter.in.event;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantStockCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkSetCommand;
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
        final var stockCountById = HashMap.<UUID, Integer>newHashMap(event.details().size());
        for (final var detail : event.details()) {
            stockCountById.put(detail.variantId(), detail.value());
        }

        final var command = new VariantStockCountBulkSetCommand(stockCountById);
        this.stockCountBulkSetUseCase.execute(command);
    }
}
