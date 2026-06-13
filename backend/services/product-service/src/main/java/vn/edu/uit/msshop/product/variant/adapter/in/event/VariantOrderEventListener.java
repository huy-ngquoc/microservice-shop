package vn.edu.uit.msshop.product.variant.adapter.in.event;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.event.payload.SetVariantSoldCountsEvent;
import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantSoldCountBulkSetCommand;
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
        final var soldCountById = HashMap.<UUID, Integer>newHashMap(event.details().size());
        for (final var detail : event.details()) {
            soldCountById.put(detail.variantId(), detail.newTotal());
        }

        final var command = new VariantSoldCountBulkSetCommand(soldCountById);
        this.soldCountBulkSetUseCase.execute(command);
    }
}
